package com.eventregistration.config;

import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class JpaConfig {

    @Bean
    AuditorAware<UUID> auditorAware() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                log.debug("No authenticated user found");
                return Optional.empty();
            }

            try {
                Object principal = authentication.getPrincipal();

                // Handle JWT authentication
                if (principal instanceof Jwt jwt) {
                    String userId = jwt.getClaimAsString("userId");
                    if (userId != null && !userId.isEmpty()) {
                        log.debug("Setting auditor from JWT userId: {}", userId);
                        return Optional.of(UUID.fromString(userId));
                    }

                    // Fallback to subject if userId claim is not available
                    String subject = jwt.getSubject();
                    if (subject != null && !subject.isEmpty()) {
                        try {
                            log.debug("Setting auditor from JWT subject: {}", subject);
                            return Optional.of(UUID.fromString(subject));
                        } catch (IllegalArgumentException e) {
                            log.warn("JWT subject is not a valid UUID: {}", subject);
                        }
                    }
                }

                // For development/testing purposes, create a deterministic UUID from username
                String username = authentication.getName();
                if (username != null && !username.equals("anonymousUser")) {
                    log.debug("Creating UUID from username: {}", username);
                    return Optional.of(UUID.nameUUIDFromBytes(username.getBytes()));
                }
            } catch (Exception e) {
                log.error("Error determining current auditor", e);
            }

            log.debug("Using system UUID as fallback");
            // Fallback to a system UUID for cases where we can't determine the user
            return Optional.of(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        };
    }
}
