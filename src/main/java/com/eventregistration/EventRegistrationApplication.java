package com.eventregistration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class EventRegistrationApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().load();
        System.out.println("Database URL: " + dotenv.get("DB_URL"));
        System.out.println("REDIS_HOST: " + dotenv.get("REDIS_HOST"));
        SpringApplication.run(EventRegistrationApplication.class, args);
    }
}
