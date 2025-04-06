package com.eventregistration.service.implement;

import java.time.Year;
import java.util.List;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.eventregistration.config.ApiKeyConfig;
import com.eventregistration.exception.AppException;
import com.eventregistration.exception.ErrorCode;
import com.eventregistration.repository.httpclient.EmailClient;
import com.eventregistration.service.EmailService;
import com.eventregistration.shared.dto.model.RecipientModel;
import com.eventregistration.shared.dto.model.SenderModel;
import com.eventregistration.shared.dto.request.EmailRequest;
import com.eventregistration.shared.dto.response.EmailResponse;

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

    private static final String OTP_TEMPLATE = "email/otp-verification";
    private static final int OTP_EXPIRATION_MINUTES = 5;

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
            log.error("Failed to send email: {}", e.getMessage(), e);
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

            // Prepare template context
            Context context = new Context();
            context.setVariable("otp", otp);
            context.setVariable("expirationMinutes", OTP_EXPIRATION_MINUTES);
            context.setVariable("currentYear", Year.now().getValue());

            // Process the template
            String htmlContent = templateEngine.process(OTP_TEMPLATE, context);

            return sendHtmlEmail(sender, List.of(recipient), "Your Verification Code for Regista", htmlContent);
        } catch (Exception e) {
            log.error("Failed to send OTP email to {}: {}", email, e.getMessage(), e);
            throw new AppException(ErrorCode.EMAIL_SENDING_FAILED);
        }
    }
}
