package com.eventregistration.service.implement;

import java.security.SecureRandom;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eventregistration.constant.AuthConstants;
import com.eventregistration.dto.request.EmailLoginRequest;
import com.eventregistration.dto.request.PasswordLoginRequest;
import com.eventregistration.dto.request.VerifyOtpRequest;
import com.eventregistration.dto.response.AuthResponse;
import com.eventregistration.dto.response.EmailLoginResponse;
import com.eventregistration.dto.response.UserResponse;
import com.eventregistration.entity.User;
import com.eventregistration.entity.UserEmail;
import com.eventregistration.exception.AppException;
import com.eventregistration.exception.ErrorCode;
import com.eventregistration.mapper.AuthenticationMapper;
import com.eventregistration.repository.UserEmailRepository;
import com.eventregistration.repository.UserRepository;
import com.eventregistration.service.AuthenticationService;
import com.eventregistration.service.EmailService;
import com.eventregistration.service.JwtService;
import com.eventregistration.service.RedisService;
import com.eventregistration.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {

    UserEmailRepository userEmailRepository;
    EmailService emailService;
    RedisService redisService;
    JwtService jwtService;
    PasswordEncoder passwordEncoder;
    UserService userService;
    AuthenticationMapper authenticationMapper;
    UserRepository userRepository;

    @Override
    public EmailLoginResponse requestEmailLogin(EmailLoginRequest request) {
        String email = request.email().toLowerCase();
        String otp = generateOtp();

        // Check if user exists with this email
        Optional<UserEmail> userEmailOpt = userEmailRepository.findByEmail(email);
        boolean isNewUser = userEmailOpt.isEmpty();
        boolean hasPassword = false;

        if (!isNewUser) {
            User user = userEmailOpt.get().getUser();
            hasPassword = user.getPassword() != null && !user.getPassword().isEmpty();
        }

        // Store OTP in Redis
        String redisKey = AuthConstants.OTP_PREFIX + email;
        redisService.set(redisKey, otp);
        redisService.setTimeToLive(redisKey, AuthConstants.OTP_EXPIRATION_SECONDS);

        // Send OTP email
        emailService.sendOtpEmail(email, otp);
        log.info("OTP email sent to: {}", email);

        // Use mapper to create response
        return authenticationMapper.toEmailLoginResponse(
                email, isNewUser, hasPassword, AuthConstants.OTP_EXPIRATION_SECONDS);
    }

    @Override
    @Transactional
    public AuthResponse verifyOtpAndLogin(VerifyOtpRequest request) {

        String email = request.email().toLowerCase().trim();
        String otp = request.otp();

        // Verify OTP from Redis
        String redisKey = AuthConstants.OTP_PREFIX + email;
        Object storedOtpObj = redisService.get(redisKey);

        if (storedOtpObj == null) {
            throw new AppException(ErrorCode.OTP_EXPIRED);
        }

        // Ensure stored OTP is a String and compare
        String storedOtp = storedOtpObj.toString(); // Hoặc redisService.get() trả về String trực tiếp
        if (!storedOtp.equals(otp)) {
            throw new AppException(ErrorCode.OTP_INVALID);
        }

        // Delete OTP after verification
        redisService.delete(redisKey);

        User user;
        boolean isNewUser = false;
        Optional<UserEmail> userEmailOpt = userEmailRepository.findByEmail(email);
        UserResponse userResponse = null;

        if (userEmailOpt.isPresent()) {
            UserEmail userEmail = userEmailOpt.get();
            userEmail.setVerified(true);
            userEmailRepository.save(userEmail);
            user = userEmail.getUser();
        } else {
            isNewUser = true;
            userResponse = userService.createNewUser(email);
        }

        // Generate tokens
        // String accessToken = jwtService.generateAccessToken(user);
        // String refreshToken = jwtService.generateRefreshToken(user);

        // Update and persist refresh token
        // user.setRefreshToken(refreshToken);
        // userRepository.save(user); // Đảm bảo lưu vào DB

        // Return response
        return authenticationMapper.toAuthResponse(userResponse, "accessToken", "refreshToken", isNewUser);
    }

    @Override
    public AuthResponse loginWithPassword(PasswordLoginRequest request) {
        return null;
        // String email = request.email().toLowerCase();

        // // Find user by email using UserService
        // User user = userService.findByEmail(email);

        // // Check if user has password
        // if (user.getPassword() == null || user.getPassword().isEmpty()) {
        //     throw new AppException(ErrorCode.PASSWORD_NOT_SET);
        // }

        // // Verify password
        // if (!passwordEncoder.matches(request.password(), user.getPassword())) {
        //     throw new AppException(ErrorCode.USER_WRONG_PASSWORD);
        // }

        // // Generate tokens
        // String accessToken = jwtService.generateAccessToken(user);
        // String refreshToken = jwtService.generateRefreshToken(user);

        // // Update refresh token in user entity
        // user.setRefreshToken(refreshToken);

        // // Use mapper to create response
        // return authenticationMapper.toAuthResponse(user, accessToken, refreshToken, false);
    }

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000); // 6-digit number
        return String.valueOf(otp);
    }
}
