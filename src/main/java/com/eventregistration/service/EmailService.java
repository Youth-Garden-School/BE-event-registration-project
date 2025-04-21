package com.eventregistration.service;

import java.util.List;

import com.eventregistration.entity.Event;
import com.eventregistration.entity.EventAttendee;
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
     * Send an email with HTML content and attachment
     *
     * @param sender The sender information
     * @param recipients List of recipients
     * @param subject Email subject
     * @param htmlContent HTML content of the email
     * @param attachmentName Name of the attachment
     * @param attachmentContent Content of the attachment as byte array
     * @param attachmentType MIME type of the attachment
     * @return Email response from the email service provider
     */
    EmailResponse sendEmailWithAttachment(
            SenderModel sender, 
            List<RecipientModel> recipients, 
            String subject, 
            String htmlContent,
            String attachmentName,
            byte[] attachmentContent,
            String attachmentType);

    /**
     * Send an OTP verification email
     *
     * @param email Recipient email address
     * @param otp One-time password
     * @return Email response from the email service provider
     */
    EmailResponse sendOtpEmail(String email, String otp);

    /**
     * Send a password reset email
     * 
     * @param email Recipient email address
     * @param resetLink Password reset link
     * @param userName Username of the recipient
     * @param expirationMinutes Expiration time in minutes
     * @return Email response from the email service provider
     */
    EmailResponse sendResetPasswordEmail(String email, String resetLink, String userName, long expirationMinutes);
    
    /**
     * Send an event registration confirmation email with ICS calendar attachment
     * 
     * @param event The event being registered for
     * @param attendee The attendee who registered
     * @return Email response from the email service provider
     */
    EmailResponse sendEventRegistrationEmail(Event event, EventAttendee attendee);
}
