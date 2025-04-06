package com.eventregistration.service;

import java.util.List;

import com.eventregistration.shared.dto.model.RecipientModel;
import com.eventregistration.shared.dto.model.SenderModel;
import com.eventregistration.shared.dto.response.EmailResponse;

public interface EmailService {
    /**
     * Send an email with HTML content
     *
     * @param sender The sender information
     * @param recipients List of recipients
     * @param subject Email subject
     * @param htmlContent HTML content of the email
     * @return Email response from the email service provider
     */
    EmailResponse sendHtmlEmail(
            SenderModel sender, List<RecipientModel> recipients, String subject, String htmlContent);

    /**
     * Send an OTP verification email
     *
     * @param email Recipient email address
     * @param otp One-time password
     * @return Email response from the email service provider
     */
    EmailResponse sendOtpEmail(String email, String otp);
}
