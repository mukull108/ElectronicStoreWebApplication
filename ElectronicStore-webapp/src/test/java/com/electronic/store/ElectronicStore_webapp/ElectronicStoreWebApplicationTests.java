package com.electronic.store.ElectronicStore_webapp;

import com.electronic.store.ElectronicStore_webapp.entities.User;
import com.electronic.store.ElectronicStore_webapp.repositories.UserRepository;
import com.electronic.store.ElectronicStore_webapp.security.JwtHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ElectronicStoreWebApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtHelper jwtHelper;

	@Test
	void testToken(){
		User user = userRepository.findByEmail("mukul@gmail.com").get();

		String token = jwtHelper.generateToken(user);
		System.out.println("Token " + token);
		System.out.println("Username from Token " + jwtHelper.getUsernameFromToken(token));
		System.out.println("token expired = " + jwtHelper.isTokenExpired(token));
	}

}
