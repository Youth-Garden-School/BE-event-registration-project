package com.eventregistration.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventregistration.dto.ApiResponse;
import com.eventregistration.dto.request.EmailSignupRequest;
import com.eventregistration.dto.request.VerifyOtpRequest;
import com.eventregistration.dto.response.EmailSignupResponse;
import com.eventregistration.service.AuthenticationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auths")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "Authentication Controller", description = "APIs for user authentication")
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/signup/email")
    @Operation(summary = "Request email signup", description = "Send OTP to user's email for verification")
    public ApiResponse<EmailSignupResponse> requestEmailSignup(@Valid @RequestBody EmailSignupRequest request) {
        log.info("Received email signup request for: {}", request.getEmail());
        EmailSignupResponse response = authenticationService.requestEmailSignup(request);
        return ApiResponse.<EmailSignupResponse>builder()
                .message("OTP sent successfully")
                .result(response)
                .build();
    }

    @PostMapping("/signup/verify")
    @Operation(
            summary = "Verify OTP and complete signup",
            description = "Verify the OTP sent to email and create user account")
    public ApiResponse<Void> verifyOtpAndSignup(@Valid @RequestBody VerifyOtpRequest request) {
        log.info("Received OTP verification for email: {}", request.getEmail());
        authenticationService.verifyOtpAndCreateUser(request);
        return ApiResponse.<Void>builder()
                .message("Email verified and account created successfully")
                .build();
    }
}
