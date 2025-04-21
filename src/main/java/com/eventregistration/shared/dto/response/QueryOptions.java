package com.eventregistration.shared.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QueryOptions {
    String sortBy;
    String sortDirection;
    String searchQuery;
}
