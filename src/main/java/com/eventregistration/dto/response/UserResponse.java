package com.eventregistration.dto.response;

import java.util.UUID;

public record UserResponse(
        UUID id, String username, String firstName, String lastName, String email, String avatarUrl, String bio) {}
