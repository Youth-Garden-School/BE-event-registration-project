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
import com.eventregistration.service.RedisService;
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
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class JwtServiceImpl implements JwtService {
    RedisService redisService;
    ApiKeyConfig apiKeyConfig;

    private String generateToken(User user, String keyType) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(String.valueOf(user.getId()))
                .issuer("Regista")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now()
                        .plus(
                                Objects.equals(keyType, "access")
                                        ? apiKeyConfig.getJwtAccessDuration()
                                        : apiKeyConfig.getJwtRefreshDuration(),
                                ChronoUnit.SECONDS)
                        .toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScopeFromRoles(user.getRoles()))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(
                    (Objects.equals(keyType, "access") ? apiKeyConfig.getJwtAccess() : apiKeyConfig.getJwtRefresh())
                            .getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String generateAccessToken(User user) {
        return generateToken(user, "access");
    }

    @Override
    public String generateRefreshToken(User user) {
        return generateToken(user, "refresh");
    }

    private SignedJWT verifyToken(String token, String keyType) throws JOSEException, ParseException {
        if (redisService.exist(token)) throw new AppException(ErrorCode.USER_UNAUTHENTICATED);

        JWSVerifier verifier = new MACVerifier(
                (Objects.equals(keyType, "access") ? apiKeyConfig.getJwtAccess() : apiKeyConfig.getJwtRefresh())
                        .getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.USER_UNAUTHENTICATED);

        return signedJWT;
    }

    @Override
    public SignedJWT verifyAccessToken(String token) throws ParseException, JOSEException {
        return verifyToken(token, "access");
    }

    @Override
    public SignedJWT verifyRefreshToken(String token) throws JOSEException, ParseException {
        return verifyToken(token, "refresh");
    }

    private String buildScopeFromRoles(Set<Role> roles) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(roles)) {
            roles.forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
                }
            });
        }

        return stringJoiner.toString();
    }
}
