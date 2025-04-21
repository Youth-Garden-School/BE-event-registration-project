package com.eventregistration.shared.dto.request;

import java.util.List;

import com.eventregistration.shared.dto.model.AttachmentModel;
import com.eventregistration.shared.dto.model.RecipientModel;
import com.eventregistration.shared.dto.model.SenderModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {
    SenderModel sender;
    List<RecipientModel> to;
    String subject;
    String htmlContent;
    List<AttachmentModel> attachment; // Added this field for attachments
}
