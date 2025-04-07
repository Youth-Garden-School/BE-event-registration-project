package com.eventregistration.constant;

/**
 * Constants related to authentication
 */
public class AuthConstants {

    // OTP related constants
    public static final String OTP_PREFIX = "signup_otp:";
    public static final int OTP_EXPIRATION_SECONDS = 300; // 5 minutes
    public static final String OTP_PATTERN = "^\\d{6}$";
    public static final int OTP_LENGTH = 6;

    // Prevent instantiation
    private AuthConstants() {
        throw new IllegalStateException("Utility class");
    }
}
