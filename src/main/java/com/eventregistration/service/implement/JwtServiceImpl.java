package com.eventregistration.service.implement;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.eventregistration.config.ApiKeyConfig;
import com.eventregistration.entity.Role;
import com.eventregistration.entity.User;
import com.eventregistration.exception.AppException;
import com.eventregistration.exception.ErrorCode;
import com.eventregistration.service.JwtService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtServiceImpl implements JwtService {

    private static final JWSAlgorithm JWS_ALGORITHM = JWSAlgorithm.HS512;
    private static final String ISSUER = "";
    private static final String PURPOSE_PASSWORD_RESET = "password-reset";
    private static final String SCOPE_PREFIX = "";

    ApiKeyConfig apiKeyConfig;

    private JWTClaimsSet.Builder createBaseClaimsBuilder(String subject) {
        return new JWTClaimsSet.Builder()
                .subject(subject)
                .issuer(ISSUER)
                .jwtID(UUID.randomUUID().toString())
                .issueTime(new Date());
    }

    private String generateToken(JWTClaimsSet claimsSet, String key) {
        try {
            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWS_ALGORITHM), claimsSet);
            signedJWT.sign(new MACSigner(key.getBytes()));

            return signedJWT.serialize();
        } catch (JOSEException e) {
            log.error("Failed to generate JWT token: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.TOKEN_GENERATION_FAILED);
        }
    }

    private SignedJWT verifyToken(String token, String key) {
        try {
            Objects.requireNonNull(token, "Token cannot be null");
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(key.getBytes());

            if (!signedJWT.verify(verifier)) {
                throw new AppException(ErrorCode.TOKEN_INVALID);
            }

            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (expiryTime == null || !expiryTime.after(new Date())) {
                throw new AppException(ErrorCode.TOKEN_EXPIRED);
            }

            return signedJWT;
        } catch (ParseException | JOSEException e) {
            log.error("Failed to verify JWT token: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.TOKEN_INVALID);
        }
    }

    @Override
    public String generateAccessToken(User user) {
        Objects.requireNonNull(user, "User cannot be null");
        JWTClaimsSet claimsSet = createBaseClaimsBuilder(user.getId().toString())
                .claim("username", user.getUsername())
                .claim("scope", buildScopeFromRoles(user.getRoles()))
                .expirationTime(createExpirationDate(apiKeyConfig.getJwtAccessDuration()))
                .build();
        return generateToken(claimsSet, apiKeyConfig.getJwtAccess());
    }

    @Override
    public String generateRefreshToken(User user) {
        Objects.requireNonNull(user, "User cannot be null");
        JWTClaimsSet claimsSet = createBaseClaimsBuilder(user.getId().toString())
                .claim("username", user.getUsername())
                .expirationTime(createExpirationDate(apiKeyConfig.getJwtRefreshDuration()))
                .build();
        return generateToken(claimsSet, apiKeyConfig.getJwtRefresh());
    }

    @Override
    public String generateResetPasswordToken(String userId, String email, Map<String, Object> additionalClaims) {
        Objects.requireNonNull(userId, "User ID cannot be null");
        Objects.requireNonNull(email, "Email cannot be null");

        JWTClaimsSet.Builder claimsBuilder = createBaseClaimsBuilder(userId)
                .claim("email", email)
                .claim("purpose", PURPOSE_PASSWORD_RESET)
                .expirationTime(createExpirationDate(apiKeyConfig.getJwtResetPasswordDuration()));

        if (!CollectionUtils.isEmpty(additionalClaims)) {
            additionalClaims.forEach((key, value) ->
                    claimsBuilder.claim(key, value instanceof java.time.LocalDateTime ldt ? ldt.toString() : value));
        }

        return generateToken(claimsBuilder.build(), apiKeyConfig.getJwtResetPassword());
    }

    @Override
    public SignedJWT verifyAccessToken(String token) {
        return verifyToken(token, apiKeyConfig.getJwtAccess());
    }

    @Override
    public SignedJWT verifyRefreshToken(String token) {
        return verifyToken(token, apiKeyConfig.getJwtRefresh());
    }

    @Override
    public SignedJWT verifyResetPasswordToken(String token) {
        return verifyToken(token, apiKeyConfig.getJwtResetPassword());
    }

    private Date createExpirationDate(long durationInSeconds) {
        return new Date(
                Instant.now().plus(durationInSeconds, ChronoUnit.SECONDS).toEpochMilli());
    }

    private String buildScopeFromRoles(Set<Role> roles) {
        if (CollectionUtils.isEmpty(roles)) {
            return "";
        }

        StringJoiner joiner = new StringJoiner(" ");
        for (Role role : roles) {
            joiner.add(SCOPE_PREFIX + role.getName());
            if (!CollectionUtils.isEmpty(role.getPermissions())) {
                role.getPermissions().forEach(permission -> joiner.add(permission.getName()));
            }
        }
        return joiner.toString();
    }
}
