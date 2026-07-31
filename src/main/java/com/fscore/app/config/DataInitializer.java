package com.fscore.app.config;

import com.fscore.app.entity.ApiUser;
import com.fscore.app.repository.ApiUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final ApiUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email:admin@football.com}")
    private String adminEmail;

    @Value("${app.admin.password:admin123}")
    private String adminPassword;

    @Value("${app.admin.name:Administrator}")
    private String adminName;

    public DataInitializer(ApiUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        String email = adminEmail.trim().toLowerCase();
        userRepository.findByEmail(email).ifPresentOrElse(user -> {
            if (user.getPasswordHash() == null) {
                user.setPasswordHash(passwordEncoder.encode(adminPassword));
                userRepository.save(user);
                log.info("Set password for existing admin user {}", email);
            }
        }, () -> {
            ApiUser admin = ApiUser.builder()
                .email(email)
                .name(adminName)
                .plan("ADMIN")
                .active(true)
                .passwordHash(passwordEncoder.encode(adminPassword))
                .build();
            userRepository.save(admin);
            log.info("Created default admin user {}", email);
        });
    }
}
