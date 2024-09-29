package com.electronic.store.ElectronicStore_webapp.controllers;

import com.electronic.store.ElectronicStore_webapp.dtos.*;
import com.electronic.store.ElectronicStore_webapp.entities.User;
import com.electronic.store.ElectronicStore_webapp.exceptions.BadApiRequestException;
import com.electronic.store.ElectronicStore_webapp.exceptions.ResourceNotFoundException;
import com.electronic.store.ElectronicStore_webapp.security.JwtHelper;
import com.electronic.store.ElectronicStore_webapp.services.RefreshTokenService;
import com.electronic.store.ElectronicStore_webapp.services.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.apache.v2.ApacheHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private ModelMapper mapper;

    @Value("${app.google.client.id}")
    private String googleClientId;

    @Value(("${app.google.default.password}"))
    private String googleProviderDefaultPassword;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RefreshTokenService refreshTokenService;


    @Autowired
    private JwtHelper jwtHelper;
    private Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/regenerate-token")
    public ResponseEntity<JwtResponse> regenerateToken(@RequestBody RefreshTokenRequest request){
        RefreshTokenDto refreshTokenDto = refreshTokenService.findByToken(request.getRefreshToken());
        RefreshTokenDto refreshTokenDto1 = refreshTokenService.verifyRefreshToken(refreshTokenDto);
        UserDto user = refreshTokenService.getUser(refreshTokenDto1);

        String jwtToken = jwtHelper.generateToken(mapper.map(user, User.class));

        JwtResponse response = JwtResponse.builder()
                .userDto(user)
                .token(jwtToken)
                .refreshToken(refreshTokenDto1)
                .build();

        return ResponseEntity.ok(response);

    }

    @PostMapping("/generate-token")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        logger.info("Username {}, Password {}", request.getUsername(), request.getPassword());

        this.doAuthenticate(request.getUsername(), request.getPassword());

        //generate token after authenticating
        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtHelper.generateToken(user);

        //refresh token
        RefreshTokenDto refreshToken = refreshTokenService.createRefreshToken(request.getUsername());

        //send response
        JwtResponse response = JwtResponse.builder()
                .token(token)
                .userDto(mapper.map(user, UserDto.class))
                .refreshToken(refreshToken)
                .build();

        return ResponseEntity.ok(response);
    }

    private void doAuthenticate(String username, String password) {
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
            authenticationManager.authenticate(authentication);
        } catch (BadCredentialsException ex) {
            throw new BadApiRequestException("Invalid username and password !!");
        }
    }

    //handle login with google
    @PostMapping("/login-with-google")
    public ResponseEntity<JwtResponse> handleGoogleLogin(@RequestBody TokenRequestGoogle loginRequest) throws GeneralSecurityException, IOException {


        //verify the token
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new ApacheHttpTransport(), new GsonFactory())
                .setAudience(List.of(googleClientId)).build();

        String token = loginRequest.getToken();

//        logger.info("token is : {}", token);
        GoogleIdToken googleIdToken = verifier.verify(token);

        if (googleIdToken != null) {
            //token verified
            GoogleIdToken.Payload payload = googleIdToken.getPayload();
            String email = payload.getEmail();
            String username = payload.getSubject();
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");

            logger.info("Name: {}, User Name: {}, Email: {}, Picture Url: {}", name, username, email, pictureUrl);

            UserDto userDto = new UserDto();
            userDto.setName(name);
            userDto.setEmail(email);
            userDto.setImageName(pictureUrl);
            userDto.setPassword(googleProviderDefaultPassword);
            userDto.setAbout("Created by Google Login");

            UserDto user = null;
            try {
                user = userService.getUserByEmail(userDto.getEmail());
                logger.info("User is loaded from the DB !!");
            } catch (ResourceNotFoundException ex) {
                user = userService.createUser(userDto);
                logger.info("This time user created");
            }
            //generate token
            this.doAuthenticate(user.getEmail(), googleProviderDefaultPassword); //can not decode password

            User user1 = mapper.map(user, User.class);//dto to entity of userDetails

            String generateToken = jwtHelper.generateToken(user1);
            //send response
            JwtResponse response = JwtResponse.builder().token(generateToken).userDto(user).build();

            return ResponseEntity.ok(response);
        } else {
            //token is invalid
            logger.info("Token is invalid!!");
            throw new BadApiRequestException("Invalid User!!");
        }
    }


}
