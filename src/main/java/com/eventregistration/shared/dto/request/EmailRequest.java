package com.eventregistration.shared.dto.request;

import java.util.List;

import com.eventregistration.shared.dto.model.RecipientModel;
import com.eventregistration.shared.dto.model.SenderModel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailRequest {
    @Schema(description = "Sender information")
    SenderModel sender;

    @Schema(description = "List of recipients")
    List<RecipientModel> to;

    @Schema(description = "Email subject", example = "Welcome to Regista")
    String subject;

    @Schema(description = "HTML content of the email")
    String htmlContent;

    @Schema(description = "Text content of the email (optional)")
    String textContent;

    @Schema(description = "Reply-to email address (optional)")
    String replyTo;
}
