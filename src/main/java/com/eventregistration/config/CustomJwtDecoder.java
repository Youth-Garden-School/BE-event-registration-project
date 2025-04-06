package com.eventregistration.config;

import java.text.ParseException;
import java.time.Instant;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import com.eventregistration.service.RedisService;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CustomJwtDecoder implements JwtDecoder {
    RedisService redisService;

    @Override
    public Jwt decode(String token) throws JwtException {
        boolean checkedToken = redisService.exist(token);

        if (checkedToken) throw new JwtException("Invalid Token");

        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
            var jwt = new Jwt(
                    token,
                    jwtClaimsSet.getIssueTime().toInstant(),
                    jwtClaimsSet.getExpirationTime().toInstant(),
                    signedJWT.getHeader().toJSONObject(),
                    jwtClaimsSet.getClaims());
            Instant expiryTime = jwt.getExpiresAt();

            if (expiryTime != null && expiryTime.isBefore(Instant.now())) {
                throw new JwtException("Token has expired");
            }

            return jwt;
        } catch (ParseException e) {
            throw new JwtException("Invalid token");
        }
    }
}
