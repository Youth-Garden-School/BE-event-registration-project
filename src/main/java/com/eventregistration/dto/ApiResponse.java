package com.eventregistration.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standard API response wrapper for all endpoints")
public class ApiResponse<T> {
    @Builder.Default
    @Schema(description = "Response code (1000 for success)", example = "1000")
    int code = 1000;

    @Schema(description = "Response message", example = "Operation completed successfully")
    String message;
    
    @Schema(description = "Response data payload")
    T result;
}
