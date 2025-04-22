package com.eventregistration.controller;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventregistration.dto.ApiResponse;
import com.eventregistration.dto.response.AuthResponse;
import com.eventregistration.service.OAuth2AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/auths/oauth2")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "OAuth2 Authentication Controller", description = "APIs for OAuth2 authentication")
public class OAuth2AuthController {

    OAuth2AuthService oAuth2AuthService;

    @GetMapping("/login/google")
    @Operation(summary = "Redirect to Google login", description = "Redirects the user to Google's OAuth2 login page")
    public void redirectToGoogleLogin(HttpServletResponse response) throws IOException {
        log.info("Redirecting to Google login");
        response.sendRedirect("/oauth2/authorization/google");
    }

    @GetMapping("/login-success")
    @Operation(
            summary = "OAuth2 login success",
            description = "Handles successful OAuth2 login and returns authentication response")
    public ApiResponse<AuthResponse> loginSuccess(@AuthenticationPrincipal OAuth2User oauth2User) {
        log.info("OAuth2 login successful for user: {}", oauth2User.getName());

        AuthResponse authResponse = oAuth2AuthService.processOAuthPostLogin(oauth2User);

        return ApiResponse.<AuthResponse>builder()
                .message("OAuth2 login successful")
                .result(authResponse)
                .build();
    }
}
