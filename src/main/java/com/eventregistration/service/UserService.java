package com.eventregistration.service;

import com.eventregistration.dto.request.ResetPasswordRequest;
import com.eventregistration.dto.request.UpdateUserRequest;
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

    /**
     * Get current user information
     *
     * @param email The email of current user
     * @return Current user information
     */
    UserResponse getCurrentUser(String email);

    /**
     * Update user profile
     *
     * @param email User's email
     * @param request Update request containing new information
     * @return Updated user information
     */
    UserResponse updateUser(String email, UpdateUserRequest request);

    /**
     * Delete user account
     *
     * @param email User's email
     */
    void deleteUser(String email);

    /**
     * Request password reset by sending email with reset link
     *
     * @param email User's email address
     * @param callbackUrl Optional callback URL for the reset link
     */
    void requestPasswordReset(String email, String callbackUrl);

    void resetPassword(ResetPasswordRequest request);
}
