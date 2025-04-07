package com.eventregistration.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.eventregistration.entity.Role;
import com.eventregistration.entity.User;
import com.eventregistration.entity.UserEmail;
import com.eventregistration.repository.RoleRepository;
import com.eventregistration.repository.UserEmailRepository;
import com.eventregistration.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    @NonFinal
    static final String ADMIN_EMAIL = "regista.admin@yopmail.com";

    @NonFinal
    static final String ADMIN_PASSWORD = "Admin@123";

    @Bean
    ApplicationRunner applicationRunner(
            UserRepository userRepository,
            RoleRepository roleRepository,
            UserEmailRepository userEmailRepository,
            PasswordEncoder passwordEncoder) {
        log.info("Initializing application...");

        return args -> {
            if (!userEmailRepository.existsByEmail(ADMIN_EMAIL)) {
                Role userRole = roleRepository
                        .findByName("ROLE_USER")
                        .orElseGet(() -> roleRepository.save(Role.builder()
                                .name("ROLE_USER")
                                .description("Default user role")
                                .build()));

                Role adminRole = roleRepository
                        .findByName("ROLE_ADMIN")
                        .orElseGet(() -> roleRepository.save(Role.builder()
                                .name("ROLE_ADMIN")
                                .description("Administrator role")
                                .build()));

                Set<Role> roles = new HashSet<>();
                roles.add(adminRole);

                User adminUser = User.builder()
                        .username("admin")
                        .firstName("Admin")
                        .lastName("User")
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .roles(roles)
                        .build();

                UserEmail adminEmail = UserEmail.builder()
                        .user(adminUser)
                        .email(ADMIN_EMAIL)
                        .isPrimary(true)
                        .isVerified(true)
                        .build();

                adminUser.setEmails(List.of(adminEmail));
                userRepository.save(adminUser);

                log.info("Admin user created with email: {}", ADMIN_EMAIL);
            } else {
                log.info("Admin user already exists with email: {}", ADMIN_EMAIL);
            }
        };
    }
}
