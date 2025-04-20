package com.eventregistration.controller;

import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventregistration.dto.ApiResponse;
import com.eventregistration.dto.request.RequestPasswordResetRequest;
import com.eventregistration.dto.request.ResetPasswordRequest;
import com.eventregistration.dto.request.UpdateUserRequest;
import com.eventregistration.dto.response.UserResponse;
import com.eventregistration.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "User Controller")
public class UserController {
    UserService userService;

    @PutMapping
    @Operation(summary = "Update current user profile")
    public ApiResponse<UserResponse> updateProfile(
            @AuthenticationPrincipal Jwt jwt, @Valid @RequestBody UpdateUserRequest request) {

        String username = jwt.getClaimAsString("username");
        log.info("Updating profile for user: {}", username);
        UserResponse response = userService.updateUser(username, request);

        return ApiResponse.<UserResponse>builder()
                .message("Profile updated successfully")
                .result(response)
                .build();
    }

    @DeleteMapping
    @Operation(summary = "Delete current user account")
    public ApiResponse<Void> deleteAccount(@AuthenticationPrincipal Jwt jwt) {

        String username = jwt.getClaimAsString("username");
        log.info("Deleting account for user: {}", username);
        userService.deleteUser(username);

        return ApiResponse.<Void>builder()
                .message("Account deleted successfully")
                .build();
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user profile")
    public ApiResponse<UserResponse> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        log.info("username: {}", jwt.getClaimAsString("username"));

        UserResponse response = userService.getCurrentUser(jwt.getClaimAsString("username"));

        return ApiResponse.<UserResponse>builder()
                .message("Profile fetched successfully")
                .result(response)
                .build();
    }

    @PostMapping("/password/reset-request")
    @Operation(summary = "Request password reset", description = "Send password reset email to user")
    public ApiResponse<Void> requestPasswordReset(@Valid @RequestBody RequestPasswordResetRequest request) {
        log.info("Received password reset request for: {}", request.email());
        userService.requestPasswordReset(request.email(), request.callbackUrl());

        return ApiResponse.<Void>builder()
                .message("Password reset email sent successfully")
                .build();
    }

    @PostMapping("/password/reset")
    @Operation(summary = "Reset password", description = "Reset password using token received via email")
    public ApiResponse<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        log.info("Received password reset confirmation");
        userService.resetPassword(request);

        return ApiResponse.<Void>builder()
                .message("Password reset successfully")
                .build();
    }
}
