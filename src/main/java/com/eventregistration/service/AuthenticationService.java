package com.eventregistration.service;

import com.eventregistration.dto.request.EmailLoginRequest;
import com.eventregistration.dto.request.PasswordLoginRequest;
import com.eventregistration.dto.request.VerifyOtpRequest;
import com.eventregistration.dto.response.AuthResponse;
import com.eventregistration.dto.response.EmailLoginResponse;

public interface AuthenticationService {

    /**
     * Request email login by sending OTP
     * If user doesn't exist, a new account will be created after OTP verification
     */
    EmailLoginResponse requestEmailLogin(EmailLoginRequest request);

    /**
     * Verify OTP and login or create a new account
     */
    AuthResponse verifyOtpAndLogin(VerifyOtpRequest request);

    /**
     * Login with email and password
     */
    AuthResponse loginWithPassword(PasswordLoginRequest request);
}
