package com.eventregistration.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

public record EmailSignupRequest(
        @NotBlank(message = "USER_EMAIL_REQUIRED")
                @Email(message = "USER_INVALID_EMAIL")
                @Schema(description = "User's email address", example = "user@example.com")
                String email) {}
