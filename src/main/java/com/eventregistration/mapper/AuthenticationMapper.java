package com.eventregistration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.eventregistration.dto.response.AuthResponse;
import com.eventregistration.dto.response.EmailLoginResponse;
import com.eventregistration.dto.response.UserResponse;
import com.eventregistration.entity.User;
import com.eventregistration.entity.UserEmail;

// @Mapper(componentModel = "spring")
@Mapper(
        componentModel = "spring",
        uses = {})
public interface AuthenticationMapper {

    /**
     * Convert parameters to EmailLoginResponse
     *
     * @param email Email address
     * @param isNewUser Whether this is a new user
     * @param hasPassword Whether the user has a password set
     * @param expirationSeconds OTP expiration time in seconds
     * @return EmailLoginResponse
     */
    EmailLoginResponse toEmailLoginResponse(
            String email, boolean isNewUser, boolean hasPassword, int expirationSeconds);

    /**
     * Convert User entity to AuthResponse
     *
     * @param user UserResponse object containing user details
     * @param accessToken JWT access token for authentication
     * @param refreshToken JWT refresh token for obtaining new access tokens
     * @param isNewUser Whether this is a new user registration
     * @return AuthResponse containing user details and tokens
     */
    @Mapping(target = "accessToken", source = "accessToken")
    @Mapping(target = "refreshToken", source = "refreshToken")
    @Mapping(target = "isNewUser", source = "isNewUser")
    @Mapping(target = "user", source = "user")
    AuthResponse toAuthResponse(UserResponse user, String accessToken, String refreshToken, boolean isNewUser);

    /**
     * Get primary email from User entity
     *
     * @param user User entity
     * @return Primary email address
     */
    default String getPrimaryEmail(User user) {
        return user.getEmails().stream()
                .filter(UserEmail::isPrimary)
                .findFirst()
                .map(UserEmail::getEmail)
                .orElse("");
    }
}
