package com.eventregistration.service.implement;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.eventregistration.config.ApiKeyConfig;
import com.eventregistration.entity.User;
import com.eventregistration.service.JwtService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
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

    ApiKeyConfig apiKeyConfig;

    @Override
    public String generateAccessToken(User user) {
        try {
            JWSSigner signer = new MACSigner(apiKeyConfig.getJwtAccess().getBytes());

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getId().toString())
                    .claim("username", user.getUsername())
                    .claim("roles", user.getRoles())
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + apiKeyConfig.getJwtAccessDuration() * 1000))
                    .build();

            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            log.error("Failed to generate access token", e);
            throw new RuntimeException("Failed to generate access token", e);
        }
    }

    @Override
    public String generateRefreshToken(User user) {
        try {
            JWSSigner signer = new MACSigner(apiKeyConfig.getJwtRefresh().getBytes());

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getId().toString())
                    .claim("username", user.getUsername())
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + apiKeyConfig.getJwtRefreshDuration() * 1000))
                    .build();

            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            log.error("Failed to generate refresh token", e);
            throw new RuntimeException("Failed to generate refresh token", e);
        }
    }

    @Override
    public String generateResetPasswordToken(String userId, String email, Map<String, Object> additionalClaims) {
        try {
            JWSSigner signer = new MACSigner(apiKeyConfig.getJwtResetPassword().getBytes());

            JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder()
                    .subject(userId)
                    .claim("email", email)
                    .claim("purpose", "password-reset")
                    .issueTime(new Date())
                    .expirationTime(
                            new Date(System.currentTimeMillis() + apiKeyConfig.getJwtResetPasswordDuration() * 1000));

            // Add additional claims if provided
            if (additionalClaims != null) {
                for (Map.Entry<String, Object> entry : additionalClaims.entrySet()) {
                    claimsBuilder.claim(entry.getKey(), entry.getValue());
                }
            }

            JWTClaimsSet claimsSet = claimsBuilder.build();

            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            log.error("Failed to generate reset password token", e);
            throw new RuntimeException("Failed to generate reset password token", e);
        }
    }

    @Override
    public SignedJWT verifyAccessToken(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(apiKeyConfig.getJwtAccess().getBytes());

        if (!signedJWT.verify(verifier)) {
            throw new JOSEException("Invalid access token signature");
        }

        return signedJWT;
    }

    @Override
    public SignedJWT verifyRefreshToken(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(apiKeyConfig.getJwtRefresh().getBytes());

        if (!signedJWT.verify(verifier)) {
            throw new JOSEException("Invalid refresh token signature");
        }

        return signedJWT;
    }

    @Override
    public SignedJWT verifyResetPasswordToken(String token) throws ParseException, JOSEException {
        JWSVerifier verifier =
                new MACVerifier(apiKeyConfig.getJwtResetPassword().getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date()))) {
            throw new JOSEException("Invalid reset password token");
        }

        return signedJWT;
    }
}
