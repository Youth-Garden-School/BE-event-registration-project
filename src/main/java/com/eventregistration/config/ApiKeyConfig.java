package com.eventregistration.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Component
@ConfigurationProperties(prefix = "api-keys")
@Getter
@Setter
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiKeyConfig {
    @NotBlank(message = "API_KEY_BREVO_REQUIRED")
    String brevo;

    @NotBlank(message = "API_KEY_PEXELS_REQUIRED")
    String pexels;

    @NotBlank(message = "API_KEY_TRANSLATE_REQUIRED")
    String translate;

    @NotBlank(message = "API_KEY_GOOGLE_CLIENT_ID_REQUIRED")
    String googleClientId;

    @NotBlank(message = "API_KEY_GOOGLE_CLIENT_SECRET_REQUIRED")
    String googleClientSecret;

    @NotBlank(message = "API_KEY_FACEBOOK_CLIENT_ID_REQUIRED")
    String facebookClientId;

    @NotBlank(message = "API_KEY_FACEBOOK_CLIENT_SECRET_REQUIRED")
    String facebookClientSecret;

    @NotBlank(message = "API_KEY_JWT_ACCESS_REQUIRED")
    String jwtAccess;

    @NotBlank(message = "API_KEY_JWT_REFRESH_REQUIRED")
    String jwtRefresh;

    @NotNull(message = "API_KEY_JWT_ACCESS_DURATION_REQUIRED")
    @Min(value = 1, message = "API_KEY_JWT_DURATION_INVALID")
    long jwtAccessDuration;

    @NotNull(message = "API_KEY_JWT_REFRESH_DURATION_REQUIRED")
    @Min(value = 1, message = "API_KEY_JWT_DURATION_INVALID")
    long jwtRefreshDuration;

    // PostgreSQL
    @NotBlank(message = "API_KEY_DB_URL_REQUIRED")
    String dbUrl;

    @NotBlank(message = "API_KEY_DB_USERNAME_REQUIRED")
    String dbUsername;

    @NotBlank(message = "API_KEY_DB_PASSWORD_REQUIRED")
    String dbPassword;

    // Redis
    @NotBlank(message = "API_KEY_REDIS_HOST_REQUIRED")
    String redisHost;

    @NotNull(message = "API_KEY_REDIS_PORT_REQUIRED")
    int redisPort;

    @NotBlank(message = "API_KEY_REDIS_PASSWORD_REQUIRED")
    String redisPassword;

    // Các thông tin khác (tuỳ chọn)
    @NotBlank(message = "API_KEY_SENDER_EMAIL_REQUIRED")
    String senderEmail;

    @NotBlank(message = "API_KEY_SENDER_NAME_REQUIRED")
    String senderName;

    @NotBlank(message = "API_KEY_FIREBASE_BUCKET_NAME_REQUIRED")
    String firebaseBucketName;
}
