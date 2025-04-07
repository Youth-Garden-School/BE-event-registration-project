package com.eventregistration.service.implement;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eventregistration.constant.AuthConstants;
import com.eventregistration.dto.request.EmailSignupRequest;
import com.eventregistration.dto.request.VerifyOtpRequest;
import com.eventregistration.dto.response.EmailSignupResponse;
import com.eventregistration.entity.User;
import com.eventregistration.entity.UserEmail;
import com.eventregistration.exception.AppException;
import com.eventregistration.exception.ErrorCode;
import com.eventregistration.mapper.AuthenticationMapper;
import com.eventregistration.repository.UserEmailRepository;
import com.eventregistration.repository.UserRepository;
import com.eventregistration.service.AuthenticationService;
import com.eventregistration.service.EmailService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {

    UserRepository userRepository;
    UserEmailRepository userEmailRepository;
    EmailService emailService;
    RedisTemplate<String, String> redisTemplate;
    AuthenticationMapper authenticationMapper;

    @Override
    public EmailSignupResponse requestEmailSignup(EmailSignupRequest request) {
        String email = request.email();

        // Check if email already exists and is verified
        if (userEmailRepository.existsByEmail(email)) {
            throw new AppException(ErrorCode.USER_EMAIL_EXISTED);
        }

        // Generate 6-digit OTP
        String otp = generateOtp();

        // Store OTP in Redis with expiration
        String redisKey = AuthConstants.OTP_PREFIX + email;
        redisTemplate.opsForValue().set(redisKey, otp, Duration.ofSeconds(AuthConstants.OTP_EXPIRATION_SECONDS));

        // Send email with OTP
        emailService.sendOtpEmail(email, otp);
        log.info("OTP email sent to: {}", email);

        return new EmailSignupResponse(email, AuthConstants.OTP_EXPIRATION_SECONDS);
    }

    @Override
    @Transactional
    public void verifyOtpAndCreateUser(VerifyOtpRequest request) {
        String email = request.email();
        String otp = request.otp();

        // Get OTP from Redis
        String redisKey = AuthConstants.OTP_PREFIX + email;
        String storedOtp = redisTemplate.opsForValue().get(redisKey);

        // Validate OTP
        if (storedOtp == null) {
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }

        if (!storedOtp.equals(otp)) {
            throw new AppException(ErrorCode.TOKEN_INVALID);
        }

        // Delete OTP from Redis after successful verification
        redisTemplate.delete(redisKey);

        // Create user with verified email
        User user = User.builder().username(generateUsername(email)).build();
        User savedUser = userRepository.save(user);

        // Use mapper to create UserEmail entity
        UserEmail userEmail = authenticationMapper.toUserEmail(request);
        userEmail.setUser(savedUser);

        userEmailRepository.save(userEmail);
        log.info("User created successfully with email: {}", email);
    }

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000); // 6-digit number
        return String.valueOf(otp);
    }

    private String generateUsername(String email) {
        // Generate username from email prefix + random suffix
        String prefix = email.split("@")[0];
        String randomSuffix = UUID.randomUUID().toString().substring(0, 8);
        return prefix + "_" + randomSuffix;
    }
}
