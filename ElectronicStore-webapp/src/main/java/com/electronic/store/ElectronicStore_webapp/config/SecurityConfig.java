package com.electronic.store.ElectronicStore_webapp.config;

import com.electronic.store.ElectronicStore_webapp.security.JwtAuthenticationEntryPoint;
import com.electronic.store.ElectronicStore_webapp.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter filter;

    @Autowired
    private JwtAuthenticationEntryPoint entryPoint;

    //security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //configurations

        //cors
//        httpSecurity.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.disable());
        httpSecurity.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource(new CorsConfigurationSource() {
                @Override
                public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();

                    corsConfiguration.setAllowedOriginPatterns(List.of("http://localhost:3000"));  // Use the actual domains
                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
                    corsConfiguration.setAllowCredentials(true);
                    corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
                    corsConfiguration.setMaxAge(3600L); // Cache preflight response for 1 hour

                    return corsConfiguration;
                }
            });
        });

        //csrf
        httpSecurity.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());

        //url configurations
        httpSecurity.authorizeHttpRequests(request ->
                        request
                                //secure user, category and products
                                .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole(AppConstants.ROLE_ADMIN)
                                .requestMatchers(HttpMethod.PUT, "/users/**").hasAnyRole(AppConstants.ROLE_ADMIN, AppConstants.ROLE_NORMAL)
                                .requestMatchers(HttpMethod.GET, "/product/**").permitAll()
                                .requestMatchers("/product/**").hasRole(AppConstants.ROLE_ADMIN)
                                .requestMatchers(HttpMethod.GET, "/users/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/users/create").permitAll()
                                .requestMatchers(HttpMethod.GET, "/category/**").permitAll()
                                .requestMatchers("/category/**").hasRole(AppConstants.ROLE_ADMIN)
                                .requestMatchers(HttpMethod.POST, "/auth/generate-token", "/auth/login-with-google").permitAll()
                                .requestMatchers("/auth/**").authenticated()
                                .anyRequest().permitAll()

//                    //secure order
//                        method level security
//                    //secure cart
//                        method level security

        );


        //security type
//        httpSecurity.httpBasic(Customizer.withDefaults());

        //jwt token security
        //entry point
        httpSecurity.exceptionHandling(ex -> ex.authenticationEntryPoint(entryPoint));
        //session creation policy
        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        //main filter will run before username password authentication filter
        httpSecurity.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }
}
