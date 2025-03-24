package com.eventregistration.config;

import com.eventregistration.entity.User;
import com.eventregistration.exception.AppException;
import com.eventregistration.exception.ErrorCode;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.server.wordwaves.entity.user.Role;
import com.server.wordwaves.entity.user.User;
import com.server.wordwaves.exception.AppException;
import com.server.wordwaves.exception.ErrorCode;
import com.server.wordwaves.service.BaseRedisService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class JwtTokenProvider {
    BaseRedisService baseRedisService;

    @NonFinal
    @Value("${jwt.access-signer-key}")
    protected String ACCESS_SIGNER_KEY;

    @NonFinal
    @Value("${jwt.refresh-signer-key}")
    protected String REFRESH_SIGNER_KEY;

    @NonFinal
    @Value("${jwt.access-token-duration-in-seconds}")
    protected long ACCESS_TOKEN_EXPIRATION;

    @NonFinal
    @Value("${jwt.refresh-token-duration-in-seconds}")
    protected long REFRESH_TOKEN_EXPIRATION;

    private String generateToken(User user, String keyType) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(String.valueOf(user.getId()))
                .issuer("wordwaves")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now()
                        .plus(
                                Objects.equals(keyType, "access") ? ACCESS_TOKEN_EXPIRATION : REFRESH_TOKEN_EXPIRATION,
                                ChronoUnit.SECONDS)
                        .toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(
                    (Objects.equals(keyType, "access") ? ACCESS_SIGNER_KEY : REFRESH_SIGNER_KEY).getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    public String generateAccessToken(User user) {
        return generateToken(user, "access");
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, "refresh");
    }

    public SignedJWT verifyToken(String token, String keyType) throws JOSEException, ParseException {
        if (baseRedisService.exist(token)) throw new AppException(ErrorCode.USER_UNAUTHENTICATED);

        JWSVerifier verifier = new MACVerifier(
                (Objects.equals(keyType, "access") ? ACCESS_SIGNER_KEY : REFRESH_SIGNER_KEY).getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.USER_UNAUTHENTICATED);

        return signedJWT;
    }

    public SignedJWT verifyAccessToken(String token) throws ParseException, JOSEException {
        return verifyToken(token, "access");
    }

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

    private String buildScope(User user) {
        return buildScopeFromRoles(user.getRoles());
    }
}
