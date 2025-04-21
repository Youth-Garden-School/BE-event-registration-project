package com.eventregistration.shared.dto.response;

import java.time.LocalDateTime;

public record BaseReponse(LocalDateTime createdAt, LocalDateTime updatedAt) {
    
}
