package com.electronic.store.ElectronicStore_webapp;

import com.electronic.store.ElectronicStore_webapp.entities.Role;
import com.electronic.store.ElectronicStore_webapp.entities.User;
import com.electronic.store.ElectronicStore_webapp.repositories.RoleRepository;
import com.electronic.store.ElectronicStore_webapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@SpringBootApplication
public class ElectronicStoreWebApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreWebApplication.class, args);
	}

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		Role roleAdmin = roleRepository.findByName("ROLE_ADMIN").orElse(null);
		Role roleNormal = roleRepository.findByName("ROLE_NORMAL").orElse(null);

		if(roleAdmin == null){
			roleAdmin = new Role();
			roleAdmin.setRoleId(UUID.randomUUID().toString());
			roleAdmin.setName("ROLE_ADMIN");

			roleRepository.save(roleAdmin);
		}

		if(roleNormal == null){
			roleNormal = new Role();
			roleNormal.setRoleId(UUID.randomUUID().toString());
			roleNormal.setName("ROLE_NORMAL");

			roleRepository.save(roleNormal);
		}

		User user = userRepository.findByEmail("mukul@gmail.com").orElse(null);
		if(user == null){
			user = new User();
			user.setName("mukul");
			user.setEmail("mukul@gmail.com");
			user.setPassword(passwordEncoder.encode("mukul"));
			user.setRoles(Set.of(roleAdmin));
			user.setUserId(UUID.randomUUID().toString());

			userRepository.save(user);

		}
	}
}
