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
    public ApplicationRunner initializeUser() {
        return args -> {
            System.out.println("AtomicAccountability application is up and running!");

        };
    }

}
