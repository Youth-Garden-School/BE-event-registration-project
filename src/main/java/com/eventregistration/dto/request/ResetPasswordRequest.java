package com.eventregistration.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import io.swagger.v3.oas.annotations.media.Schema;

public record ResetPasswordRequest(
        @NotBlank(message = "TOKEN_REQUIRED")
                @Schema(
                        description = "Reset token received via email",
                        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
                String token,
        @NotBlank(message = "PASSWORD_REQUIRED")
                @Pattern(
                        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
                        message = "USER_WRONG_FORMAT_PASSWORD")
                @Schema(
                        description =
                                "New password (min 8 chars, must contain digits, lowercase, uppercase, and special chars)",
                        example = "Password@123")
                String password,
        @NotBlank(message = "CONFIRM_PASSWORD_REQUIRED")
                @Schema(description = "Confirm password (must match password)", example = "Password@123")
                String confirmPassword) {}
