package com.eventregistration.service;

import com.eventregistration.dto.response.UserResponse;

public interface UserService {
    /**
     * Create a new user with the given email
     *
     * @param email The email address for the new user
     * @return The created User entity
     */
    UserResponse createNewUser(String email);

    /**
     * Find a user by email
     *
     * @param email The email to search for
     * @return The User entity if found
     */
    UserResponse findByEmail(String email);
}
