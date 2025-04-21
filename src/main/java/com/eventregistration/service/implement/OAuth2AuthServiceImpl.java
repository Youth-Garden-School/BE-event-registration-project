package com.eventregistration.service.implement;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eventregistration.dto.response.AuthResponse;
import com.eventregistration.dto.response.UserResponse;
import com.eventregistration.entity.Role;
import com.eventregistration.entity.User;
import com.eventregistration.entity.UserEmail;
import com.eventregistration.mapper.UserMapper;
import com.eventregistration.repository.RoleRepository;
import com.eventregistration.repository.UserEmailRepository;
import com.eventregistration.repository.UserRepository;
import com.eventregistration.service.JwtService;
import com.eventregistration.service.OAuth2AuthService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OAuth2AuthServiceImpl implements OAuth2AuthService {

    UserRepository userRepository;
    UserEmailRepository userEmailRepository;
    RoleRepository roleRepository;
    JwtService jwtService;
    UserMapper userMapper;

    @Override
    @Transactional
    public AuthResponse processOAuthPostLogin(OAuth2User oauth2User) {
        Map<String, Object> attributes = oauth2User.getAttributes();
        String email = (String) attributes.get("email");
        String firstName = (String) attributes.get("given_name");
        String lastName = (String) attributes.get("family_name");
        String picture = (String) attributes.get("picture");

        log.info("Processing OAuth2 login for email: {}", email);

        // Check if user exists
        Optional<UserEmail> userEmailOpt = userEmailRepository.findByEmail(email);
        User user;
        boolean isNewUser = false;

        if (userEmailOpt.isPresent()) {
            // User exists, update information if needed
            user = userEmailOpt.get().getUser();
            log.info("Existing user found with ID: {}", user.getId());

            // Update user avatar URL if it's changed
            if (picture != null && !picture.equals(user.getAvatarUrl())) {
                user.setAvatarUrl(picture);
                user.setUpdatedAt(LocalDateTime.now());
                user = userRepository.save(user);
            }
        } else {
            // Create new user
            log.info("Creating new user for OAuth2 login with email: {}", email);
            isNewUser = true;

            // Get default user role
            Role userRole = roleRepository
                    .findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Default role not found"));

            Set<Role> roles = new HashSet<>();
            roles.add(userRole);

            // Generate a unique username based on email
            String username = generateUniqueUsername(email);

            // Create user
            user = User.builder()
                    .username(username)
                    .firstName(firstName)
                    .lastName(lastName)
                    .avatarUrl(picture) // Sử dụng avatarUrl thay vì profilePicture
                    .roles(roles)
                    .build();

            // Create and associate email
            UserEmail userEmail = UserEmail.builder()
                    .user(user)
                    .email(email)
                    .isPrimary(true)
                    .isVerified(true)
                    .build();

            user = userRepository.save(user);
            userEmailRepository.save(userEmail);

            log.info("New user created with ID: {}", user.getId());
        }

        // Map user to UserResponse
        UserResponse userResponse = userMapper.toUserResponse(user);

        // Generate JWT tokens
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // Create and return AuthResponse
        return new AuthResponse(accessToken, refreshToken, isNewUser, userResponse);
    }

    private String generateUniqueUsername(String email) {
        // Extract part before @ in email
        String baseUsername = email.split("@")[0];

        // Check if username exists
        if (!userRepository.existsByUsername(baseUsername)) {
            return baseUsername;
        }

        // If exists, append random suffix
        return baseUsername + "_" + UUID.randomUUID().toString().substring(0, 8);
    }
}
