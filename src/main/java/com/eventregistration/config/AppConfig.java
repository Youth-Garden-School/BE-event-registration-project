package com.eventregistration.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.cloud.openfeign.EnableFeignClients;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableWebSecurity
@EnableMethodSecurity
@EnableFeignClients(basePackages = "com.eventregistration")
public class AppConfig {}
