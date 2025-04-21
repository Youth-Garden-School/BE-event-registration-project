package com.eventregistration.shared.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentModel {
    private String name;
    private String content; // Base64 encoded content
    private String type;    // MIME type
}