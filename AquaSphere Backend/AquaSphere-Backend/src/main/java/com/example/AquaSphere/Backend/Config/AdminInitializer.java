package com.example.AquaSphere.Backend.Config;

import com.example.AquaSphere.Backend.Entity.User;
import com.example.AquaSphere.Backend.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class AdminInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @Profile("!prod") // Only run in non-production environments
    public CommandLineRunner initializeAdmin() {
        return args -> {
            // Check if admin already exists
            if (!userRepository.existsByEmail("admin@aquasphere.com")) {
                User admin = new User();
                admin.setName("Admin");
                admin.setEmail("admin@aquasphere.com");
                admin.setPassword(passwordEncoder.encode("adminPassword123"));
                admin.setRole("ADMIN");
                admin.setAddress("Admin Address");
                admin.setPostalCode("10000");
                admin.setNic("ADMIN001");
                admin.setContactNo("1234567890");
                admin.setOtp("000000");
                admin.setOtpGeneratedTime(LocalDateTime.now());

                userRepository.save(admin);
                System.out.println("Admin user initialized");
            }
        };
    }
}