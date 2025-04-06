package com.eventregistration.service;

import com.eventregistration.dto.request.EmailSignupRequest;
import com.eventregistration.dto.request.VerifyOtpRequest;
import com.eventregistration.dto.response.EmailSignupResponse;

public interface AuthenticationService {
    /**
     * Request email signup by sending OTP to the provided email
     *
     * @param request The email signup request
     * @return Response containing email and OTP expiration time
     */
    EmailSignupResponse requestEmailSignup(EmailSignupRequest request);

    /**
     * Verify OTP and create user account
     *
     * @param request The OTP verification request
     */
    void verifyOtpAndCreateUser(VerifyOtpRequest request);
}
