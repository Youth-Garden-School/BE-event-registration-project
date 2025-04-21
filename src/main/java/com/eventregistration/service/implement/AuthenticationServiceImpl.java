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
import com.eventregistration.mapper.UserMapper;
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
    UserMapper userMapper;

    @Override
    public EmailLoginResponse requestEmailLogin(EmailLoginRequest request) {
        String email = request.email().toLowerCase();
        String otp = generateOtp();

        Optional<UserEmail> userEmailOpt = userEmailRepository.findByEmail(email);
        boolean isNewUser = userEmailOpt.isEmpty();
        boolean hasPassword = false;

        if (!isNewUser) {
            User user = userEmailOpt.get().getUser();
            hasPassword = user.getPassword() != null && !user.getPassword().isEmpty();
        }

        String redisKey = AuthConstants.OTP_PREFIX + email;
        redisService.set(redisKey, otp);
        redisService.setTimeToLive(redisKey, AuthConstants.OTP_EXPIRATION_SECONDS);

        emailService.sendOtpEmail(email, otp);
        log.info("OTP email sent to: {}", email);

        return authenticationMapper.toEmailLoginResponse(
                email, isNewUser, hasPassword, AuthConstants.OTP_EXPIRATION_SECONDS);
    }

    @Override
    @Transactional
    public AuthResponse verifyOtpAndLogin(VerifyOtpRequest request) {
        String email = request.email().toLowerCase().trim();
        String otp = request.otp();

        String redisKey = AuthConstants.OTP_PREFIX + email;
        Object storedOtpObj = redisService.get(redisKey);

        if (storedOtpObj == null) {
            throw new AppException(ErrorCode.OTP_EXPIRED);
        }

        String storedOtp = storedOtpObj.toString();
        if (!storedOtp.equals(otp)) {
            throw new AppException(ErrorCode.OTP_INVALID);
        }

        redisService.delete(redisKey);

        User user;
        UserResponse userResponse;
        boolean isNewUser = false;
        Optional<UserEmail> userEmailOpt = userEmailRepository.findByEmail(email);

        if (userEmailOpt.isPresent()) {
            UserEmail userEmail = userEmailOpt.get();
            userEmail.setVerified(true);
            userEmailRepository.save(userEmail);
            user = userEmail.getUser();
            userResponse = userMapper.toUserResponse(user);
        } else {
            isNewUser = true;
            userResponse = userService.createNewUser(email);
            user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return authenticationMapper.toAuthResponse(userResponse, accessToken, refreshToken, isNewUser);
    }

    @Override
    @Transactional
    public AuthResponse loginWithPassword(PasswordLoginRequest request) {
        String email = request.email().toLowerCase().trim();

        UserEmail userEmail =
                userEmailRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        User user = userEmail.getUser();

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new AppException(ErrorCode.USER_PASSWORD_NOT_SET);
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new AppException(ErrorCode.USER_WRONG_PASSWORD);
        }

        log.info("User logged in successfully: {}", user.getEmails());
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        user.setRefreshToken(refreshToken);

        UserResponse userResponse = userMapper.toUserResponse(user);

        return authenticationMapper.toAuthResponse(userResponse, accessToken, refreshToken, false);
    }

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}
