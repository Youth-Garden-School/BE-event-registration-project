package com.eventregistration.service;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.eventregistration.dto.response.AuthResponse;

public interface OAuth2AuthService {

    /**
     * Process OAuth2 login and generate authentication response
     *
     * @param oauth2User The authenticated OAuth2 user
     * @return Authentication response with tokens and user info
     */
    AuthResponse processOAuthPostLogin(OAuth2User oauth2User);
}
