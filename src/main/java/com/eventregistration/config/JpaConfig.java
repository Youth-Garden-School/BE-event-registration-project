package com.eventregistration.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class JpaConfig {

    @Bean
    AuditorAware<String> auditorAware() {
        return () -> {
            String username = SecurityContextHolder.getContext().getAuthentication() != null
                    ? SecurityContextHolder.getContext().getAuthentication().getName()
                    : "system";
            return Optional.ofNullable(username);
        };
    }
}
