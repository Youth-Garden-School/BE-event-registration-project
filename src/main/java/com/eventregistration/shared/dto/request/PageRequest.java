package com.eventregistration.shared.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageRequest {
    
    @Schema(description = "Page number (0-based)", example = "0", defaultValue = "0")
    @Min(value = 0, message = "Page number must be greater than or equal to 0")
    @Builder.Default
    private Integer page = 0;
    
    @Schema(description = "Page size", example = "10", defaultValue = "10")
    @Min(value = 1, message = "Page size must be greater than 0")
    @Max(value = 100, message = "Page size must be less than or equal to 100")
    @Builder.Default
    private Integer size = 10;
    
    @Schema(description = "Sort field", example = "createdAt", defaultValue = "createdAt")
    @Builder.Default
    private String sortBy = "createdAt";
    
    @Schema(description = "Sort direction", example = "DESC", defaultValue = "DESC")
    @Builder.Default
    private Sort.Direction direction = Sort.Direction.DESC;
    
    public org.springframework.data.domain.PageRequest toPageable() {
        return org.springframework.data.domain.PageRequest.of(page, size, direction, sortBy);
    }
}