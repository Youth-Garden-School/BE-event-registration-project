package com.eventregistration.service.implement;

import java.time.Year;
import java.util.List;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.eventregistration.config.ApiKeyConfig;
import com.eventregistration.constant.AuthConstants;
import com.eventregistration.exception.AppException;
import com.eventregistration.exception.ErrorCode;
import com.eventregistration.repository.httpclient.EmailClient;
import com.eventregistration.service.EmailService;
import com.eventregistration.shared.dto.model.RecipientModel;
import com.eventregistration.shared.dto.model.SenderModel;
import com.eventregistration.shared.dto.request.EmailRequest;
import com.eventregistration.shared.dto.response.EmailResponse;
import com.eventregistration.util.NetworkUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailServiceImpl implements EmailService {

    EmailClient emailClient;
    ApiKeyConfig apiKeyConfig;
    TemplateEngine templateEngine;

    @Override
    public EmailResponse sendHtmlEmail(
            SenderModel sender, List<RecipientModel> recipients, String subject, String htmlContent) {
        try {
            EmailRequest request = EmailRequest.builder()
                    .sender(sender)
                    .to(recipients)
                    .subject(subject)
                    .htmlContent(htmlContent)
                    .build();

            EmailResponse response = emailClient.sendEmail(apiKeyConfig.getBrevo(), request);
            log.info("Email sent successfully to {} recipients", recipients.size());
            return response;
        } catch (Exception e) {
            String serverIp = NetworkUtils.getServerIpAddress();
            log.error("Failed to send email from server IP {}: {}", serverIp, e.getMessage(), e);
            log.error("Please add this IP address to the authorized IPs in your email service provider: {}", serverIp);
            throw new AppException(ErrorCode.EMAIL_SENDING_FAILED);
        }
    }

    @Override
    public EmailResponse sendOtpEmail(String email, String otp) {
        try {
            SenderModel sender = SenderModel.builder()
                    .name(apiKeyConfig.getSenderName())
                    .email(apiKeyConfig.getSenderEmail())
                    .build();

            RecipientModel recipient = RecipientModel.builder().email(email).build();

            Context context = new Context();
            context.setVariable("otp", otp);
            context.setVariable("expirationMinutes", AuthConstants.OTP_EXPIRATION_SECONDS / 60);
            context.setVariable("currentYear", Year.now().getValue());

            // Add logging to debug template processing
            log.info("Processing template: email/otp-verification");
            String htmlContent = templateEngine.process("email/otp-verification", context);

            // Log a snippet of the generated HTML to verify content
            log.info(
                    "Generated HTML content (first 100 chars): {}",
                    htmlContent != null ? htmlContent.substring(0, Math.min(100, htmlContent.length())) : "null");

            return sendHtmlEmail(sender, List.of(recipient), "Your Verification Code for Regista", htmlContent);
        } catch (Exception e) {
            String serverIp = NetworkUtils.getServerIpAddress();
            log.error("Failed to send OTP email to {} from server IP {}: {}", email, serverIp, e.getMessage(), e);
            throw new AppException(ErrorCode.EMAIL_SENDING_FAILED);
        }
    }

    // If you have the sendResetPasswordEmail method, update it similarly
    @Override
    public EmailResponse sendResetPasswordEmail(
            String email, String resetLink, String userName, long expirationMinutes) {
        try {
            SenderModel sender = SenderModel.builder()
                    .name(apiKeyConfig.getSenderName())
                    .email(apiKeyConfig.getSenderEmail())
                    .build();

            RecipientModel recipient = RecipientModel.builder().email(email).build();

            Context context = new Context();
            context.setVariable("name", userName);
            context.setVariable("resetLink", resetLink);
            context.setVariable("expirationMinutes", expirationMinutes);
            context.setVariable("currentYear", Year.now().getValue());

            log.info("Processing template: email/password-reset");
            String htmlContent = templateEngine.process("email/password-reset", context);

            log.info(
                    "Generated reset password HTML content (first 100 chars): {}",
                    htmlContent != null ? htmlContent.substring(0, Math.min(100, htmlContent.length())) : "null");

            return sendHtmlEmail(sender, List.of(recipient), "Reset Your Password", htmlContent);
        } catch (Exception e) {
            String serverIp = NetworkUtils.getServerIpAddress();
            log.error(
                    "Failed to send reset password email to {} from server IP {}: {}",
                    email,
                    serverIp,
                    e.getMessage(),
                    e);
            throw new AppException(ErrorCode.EMAIL_SENDING_FAILED);
        }
    }
}
