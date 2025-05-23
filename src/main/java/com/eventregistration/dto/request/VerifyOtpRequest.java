package com.eventregistration.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import com.eventregistration.constant.AuthConstants;

import io.swagger.v3.oas.annotations.media.Schema;

public record VerifyOtpRequest(
        @NotBlank(message = "USER_EMAIL_REQUIRED")
                @Email(message = "USER_INVALID_EMAIL")
                @Schema(description = "User's email address", example = "user@example.com")
                String email,
        @NotBlank(message = "OTP_EMPTY")
                @Pattern(regexp = AuthConstants.OTP_PATTERN, message = "OTP_INVALID")
                @Schema(description = "6-digit OTP received in email", example = "123456")
                String otp) {}
