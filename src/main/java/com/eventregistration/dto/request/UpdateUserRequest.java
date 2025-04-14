package com.eventregistration.dto.request;

import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateUserRequest(
        @Size(max = 50, message = "FIRST_NAME_TOO_LONG") @Schema(description = "User's first name", example = "John")
                String firstName,
        @Size(max = 50, message = "LAST_NAME_TOO_LONG") @Schema(description = "User's last name", example = "Doe")
                String lastName,
        @Size(max = 500, message = "BIO_TOO_LONG")
                @Schema(description = "User's biography", example = "Software developer passionate about...")
                String bio,
        @Schema(description = "User's avatar URL", example = "https://example.com/avatar.jpg") String avatarUrl) {}
