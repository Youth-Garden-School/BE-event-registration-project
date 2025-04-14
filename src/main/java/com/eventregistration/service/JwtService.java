package com.eventregistration.service;

import java.text.ParseException;
import java.util.Map;

import com.eventregistration.entity.User;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

public interface JwtService {
    /**
     * Generate JWT access token for a user
     *
     * @param user The user to generate token for
     * @return JWT access token
     */
    String generateAccessToken(User user);

    /**
     * Generate JWT refresh token for a user
     *
     * @param user The user to generate token for
     * @return JWT refresh token
     */
    String generateRefreshToken(User user);

    /**
     * Generate JWT reset password token
     *
     * @param userId User ID
     * @param email User email
     * @param additionalClaims Additional claims to include in the token
     * @return JWT reset password token
     */
    String generateResetPasswordToken(String userId, String email, Map<String, Object> additionalClaims);

    /**
     * Verify JWT access token
     *
     * @param token JWT access token to verify
     * @return Verified SignedJWT object
     * @throws ParseException If token cannot be parsed
     * @throws JOSEException If token verification fails
     */
    SignedJWT verifyAccessToken(String token) throws ParseException, JOSEException;

    /**
     * Verify JWT refresh token
     *
     * @param token JWT refresh token to verify
     * @return Verified SignedJWT object
     * @throws ParseException If token cannot be parsed
     * @throws JOSEException If token verification fails
     */
    SignedJWT verifyRefreshToken(String token) throws ParseException, JOSEException;

    /**
     * Verify JWT reset password token
     *
     * @param token JWT reset password token to verify
     * @return Verified SignedJWT object
     * @throws ParseException If token cannot be parsed
     * @throws JOSEException If token verification fails
     */
    SignedJWT verifyResetPasswordToken(String token) throws ParseException, JOSEException;
}
