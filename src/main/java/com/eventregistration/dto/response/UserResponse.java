package com.eventregistration.dto.response;

import java.util.Set;
import java.util.UUID;

import com.eventregistration.entity.Role;

public record UserResponse(
        UUID id,
        String username,
        String firstName,
        String lastName,
        String email,
        String avatarUrl,
        String bio,
        Set<Role> roles) {}
