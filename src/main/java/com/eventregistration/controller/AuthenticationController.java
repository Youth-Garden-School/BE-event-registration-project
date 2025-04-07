package com.eventregistration.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventregistration.dto.ApiResponse;
import com.eventregistration.dto.request.EmailLoginRequest;
import com.eventregistration.dto.request.PasswordLoginRequest;
import com.eventregistration.dto.request.VerifyOtpRequest;
import com.eventregistration.dto.response.AuthResponse;
import com.eventregistration.dto.response.EmailLoginResponse;
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

    @PostMapping("/login/email")
    @Operation(
            summary = "Request email login",
            description = "Send OTP to user's email for login or create a new account")
    public ApiResponse<EmailLoginResponse> requestEmailLogin(@Valid @RequestBody EmailLoginRequest request) {
        log.info("Received email login request for: {}", request.email());
        EmailLoginResponse response = authenticationService.requestEmailLogin(request);

        return ApiResponse.<EmailLoginResponse>builder()
                .message("Login via Email")
                .result(response)
                .build();
    }

    @PostMapping("/login/verify")
    @Operation(
            summary = "Verify OTP and login",
            description = "Verify the OTP sent to email and login or create account")
    public ApiResponse<AuthResponse> verifyOtpAndLogin(@Valid @RequestBody VerifyOtpRequest request) {
        log.info("Received OTP verification for email: {}", request.email());
        AuthResponse response = authenticationService.verifyOtpAndLogin(request);

        return ApiResponse.<AuthResponse>builder()
                .message("Login successful")
                .result(response)
                .build();
    }

    @PostMapping("/login/password")
    @Operation(summary = "Login with password", description = "Login using email and password")
    public ApiResponse<AuthResponse> loginWithPassword(@Valid @RequestBody PasswordLoginRequest request) {
        log.info("Received password login request for: {}", request.email());
        AuthResponse response = authenticationService.loginWithPassword(request);

        return ApiResponse.<AuthResponse>builder()
                .message("Login successful")
                .result(response)
                .build();
    }
}
