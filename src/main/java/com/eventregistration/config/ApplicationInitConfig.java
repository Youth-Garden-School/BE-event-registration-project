package com.eventregistration.config;

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
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    @NonFinal
    static final String ADMIN_EMAIL = "regista.admin@yopmail.com";
    @NonFinal
    static final String ADMIN_PASSWORD = "Admin@123"; // Mật khẩu mặc định

    @Bean
    ApplicationRunner applicationRunner(
            UserRepository userRepository,
            RoleRepository roleRepository,
            UserEmailRepository userEmailRepository, // Thêm UserEmailRepository
            PasswordEncoder passwordEncoder) {
        log.info("Initializing application...");

        return args -> {
            // Kiểm tra xem email admin đã tồn tại chưa
            if (!userEmailRepository.existsByEmail(ADMIN_EMAIL)) {
                // Tạo role USER nếu chưa tồn tại
                Role userRole = roleRepository.findByName("ROLE_USER")
                        .orElseGet(() -> roleRepository.save(
                                Role.builder().name("ROLE_USER").description("Default user role").build()
                        ));

                // Tạo role ADMIN nếu chưa tồn tại
                Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                        .orElseGet(() -> roleRepository.save(
                                Role.builder().name("ROLE_ADMIN").description("Administrator role").build()
                        ));

                // Tạo user admin
                Set<Role> roles = new HashSet<>();
                roles.add(adminRole);

                User adminUser = User.builder()
                        .username("admin")
                        .firstName("Admin")
                        .lastName("User")
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .roles(roles)
                        .build();

                // Tạo email cho admin
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