package com.eventregistration.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableWebSecurity
@EnableMethodSecurity
public class AppConfig {

}