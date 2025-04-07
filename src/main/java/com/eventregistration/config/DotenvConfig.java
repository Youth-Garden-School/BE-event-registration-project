package com.eventregistration.config;

import jakarta.annotation.PostConstruct;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class DotenvConfig {

    @PostConstruct
    public void init() {
        Dotenv dotenv = Dotenv.configure().load();
        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
        });
        log.error("REDIS_PORT from System: " + System.getProperty("REDIS_PORT"));
    }

    @Bean
    Dotenv dotenv() {
        return Dotenv.configure().ignoreIfMissing().load();
    }
}
