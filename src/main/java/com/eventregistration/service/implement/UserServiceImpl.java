package com.eventregistration.service.implement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eventregistration.dto.response.UserResponse;
import com.eventregistration.entity.User;
import com.eventregistration.entity.UserEmail;
import com.eventregistration.exception.AppException;
import com.eventregistration.exception.ErrorCode;
import com.eventregistration.mapper.UserMapper;
import com.eventregistration.repository.RoleRepository;
import com.eventregistration.repository.UserEmailRepository;
import com.eventregistration.repository.UserRepository;
import com.eventregistration.service.UserService;

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

    @Override
    @Transactional
    public UserResponse createNewUser(String email) {
        User user = User.builder().username(generateUsername(email)).build();
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

    private String generateUsername(String email) {
        // Generate username from email (e.g., john.doe@example.com -> john.doe)
        String username = email.substring(0, email.indexOf('@'));

        // Check if username exists
        if (userRepository.existsByUsername(username)) {
            // Append random number
            username = username + UUID.randomUUID().toString().substring(0, 4);
        }

        return username;
    }
}
