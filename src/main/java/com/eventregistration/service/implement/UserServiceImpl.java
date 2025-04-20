package com.eventregistration.service.implement;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eventregistration.config.ApiKeyConfig;
import com.eventregistration.config.EnvConfig;
import com.eventregistration.dto.request.ResetPasswordRequest;
import com.eventregistration.dto.request.UpdateUserRequest;
import com.eventregistration.dto.response.UserResponse;
import com.eventregistration.entity.User;
import com.eventregistration.entity.UserEmail;
import com.eventregistration.exception.AppException;
import com.eventregistration.exception.ErrorCode;
import com.eventregistration.mapper.UserMapper;
import com.eventregistration.repository.RoleRepository;
import com.eventregistration.repository.UserEmailRepository;
import com.eventregistration.repository.UserRepository;
import com.eventregistration.service.EmailService;
import com.eventregistration.service.JwtService;
import com.eventregistration.service.UserService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    UserEmailRepository userEmailRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    JwtService jwtService;
    PasswordEncoder passwordEncoder;
    EmailService emailService;
    ApiKeyConfig apiKeyConfig;
    EnvConfig envConfig;

    @Override
    @Transactional
    public UserResponse createNewUser(String email) {
        User user = User.builder().username(generateUsername()).build();
        User persistedUser = userRepository.save(user);
        log.info("User saved: {}", persistedUser);

        UserEmail userEmail = userEmailRepository.save(UserEmail.builder()
                .email(email)
                .user(persistedUser)
                .isPrimary(true)
                .isVerified(true)
                .build());

        persistedUser.setEmails(new ArrayList<>(Collections.singletonList(userEmail))); // Sửa ở đây
        log.info("Emails set on user");

        roleRepository.findByName("ROLE_USER").ifPresent(role -> {
            role.getUsers().add(persistedUser);
            if (persistedUser.getRoles() == null) {
                persistedUser.setRoles(new HashSet<>());
            }
            persistedUser.getRoles().add(role);
            log.info("Role set on user");
        });

        return userMapper.toUserResponse(persistedUser);
    }

    @Override
    public UserResponse findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateUser(String username, UpdateUserRequest request) {
        User user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (request.firstName() != null) user.setFirstName(request.firstName());
        if (request.lastName() != null) user.setLastName(request.lastName());
        if (request.bio() != null) user.setBio(request.bio());
        if (request.avatarUrl() != null) user.setAvatarUrl(request.avatarUrl());

        User updatedUser = userRepository.save(user);
        return userMapper.toUserResponse(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(String username) {
        User user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Remove user from roles to maintain referential integrity
        user.getRoles().forEach(role -> role.getUsers().remove(user));

        userRepository.delete(user);
    }

    @Retryable(
            value = {AppException.class},
            maxAttempts = 10,
            backoff = @Backoff(delay = 1000))
    private String generateUsername() {
        String[] prefixes = {"Sunny", "Happy", "Cool", "Star", "Blue", "Swift", "Bright", "Lucky"};
        String[] suffixes = {"Wolf", "Star", "Fox", "Sky", "Cloud", "Breeze", "Moon", "River"};
        Random random = new Random();

        String username = String.format(
                "%s%s%d",
                prefixes[random.nextInt(prefixes.length)],
                suffixes[random.nextInt(suffixes.length)],
                100 + random.nextInt(900));
        if (userRepository.existsByUsername(username)) {
            throw new AppException(ErrorCode.USER_USERNAME_GENERATION_FAILED, "Unable to generate unique username");
        }

        return username;
    }

    @Override
    public UserResponse getCurrentUser(String username) {
        User user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    @Override
    public void requestPasswordReset(String email, String callbackUrl) {
        email = email.toLowerCase().trim();

        // Check if user exists
        UserEmail userEmail =
                userEmailRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        User user = userEmail.getUser();

        try {
            // Generate JWT token for password reset using our JwtService
            Map<String, Object> additionalClaims = new HashMap<>();
            additionalClaims.put("tokenType", "PASSWORD_RESET");

            String token = jwtService.generateResetPasswordToken(user.getId().toString(), email, additionalClaims);

            // Create reset link with the provided callback URL or use the configured client URL
            String resetLink;
            if (callbackUrl != null && !callbackUrl.isEmpty()) {
                resetLink = envConfig.getClientUrl() + callbackUrl + "?token=" + token;
            } else {
                resetLink = envConfig.getClientUrl() + "?token=" + token;
            }

            // Get user's name or use "User" as fallback
            String userName = user.getFirstName() != null ? user.getFirstName() : "User";

            // Calculate expiration in minutes
            long expirationMinutes = apiKeyConfig.getJwtResetPasswordDuration() / 60;

            // Send reset password email
            emailService.sendResetPasswordEmail(email, resetLink, userName, expirationMinutes);

            log.info("Password reset email sent to: {}", email);
        } catch (Exception e) {
            log.error("Failed to send password reset email", e);
            throw new AppException(ErrorCode.EMAIL_SENDING_FAILED);
        }
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        // Validate password match
        if (!request.password().equals(request.confirmPassword())) {
            throw new AppException(ErrorCode.USER_PASSWORD_MISMATCH);
        }

        try {
            // Verify token using our JwtService
            SignedJWT signedJWT = jwtService.verifyResetPasswordToken(request.token());

            // Extract claims
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

            // Check if token is expired
            Date expirationTime = claimsSet.getExpirationTime();
            if (expirationTime != null && expirationTime.before(new Date())) {
                throw new AppException(ErrorCode.TOKEN_EXPIRED);
            }

            // Extract user ID and email
            String userId = claimsSet.getSubject();
            String email = claimsSet.getStringClaim("email");
            String purpose = claimsSet.getStringClaim("purpose");

            if (userId == null || email == null || !"password-reset".equals(purpose)) {
                throw new AppException(ErrorCode.TOKEN_INVALID);
            }

            // Find user
            User user = userRepository
                    .findById(UUID.fromString(userId))
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

            // Verify email
            boolean emailMatches =
                    user.getEmails().stream().anyMatch(e -> e.getEmail().equals(email));

            if (!emailMatches) {
                throw new AppException(ErrorCode.TOKEN_INVALID);
            }

            // Update password
            String encodedPassword = passwordEncoder.encode(request.password());
            user.setPassword(encodedPassword);
            userRepository.save(user);

            log.info("Password reset successfully for user: {}", user.getUsername());
        } catch (ParseException | JOSEException e) {
            log.error("Failed to verify reset token", e);
            throw new AppException(ErrorCode.TOKEN_INVALID);
        }
    }
}
