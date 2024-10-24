package com.atomicaccountability.atomic_accountability;

import com.atomicaccountability.atomic_accountability.entity.User;
import com.atomicaccountability.atomic_accountability.enums.NotificationPref;
import com.atomicaccountability.atomic_accountability.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.UUID;

@SpringBootApplication
public class AtomicAccountabilityApplication {

	@Autowired
	private UserRepository userRepository;
	public static void main(String[] args) {
		SpringApplication.run(AtomicAccountabilityApplication.class, args);
	}
	@Bean
	public ApplicationRunner initializeUser(){
		final User defaultUser1 = new User();
		defaultUser1.setUsername(UUID.randomUUID().toString());
		defaultUser1.setFirstName(UUID.randomUUID().toString());
		defaultUser1.setLastName(UUID.randomUUID().toString());
		defaultUser1.setEmail(UUID.randomUUID().toString());
		defaultUser1.setTimezone("UTC");
		defaultUser1.setNotificationPref(NotificationPref.TEXT);

		final User defaultUser2 = new User();
		defaultUser2.setUsername(UUID.randomUUID().toString());
		defaultUser2.setFirstName(UUID.randomUUID().toString());
		defaultUser2.setLastName(UUID.randomUUID().toString());
		defaultUser2.setEmail(UUID.randomUUID().toString());
		defaultUser2.setTimezone("UTC");
		defaultUser2.setNotificationPref(NotificationPref.EMAIL);



		return args -> userRepository.saveAll(Arrays.asList(defaultUser1, defaultUser2));


	}

}
