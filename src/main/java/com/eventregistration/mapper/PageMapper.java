package com.eventregistration.mapper;

import com.eventregistration.shared.dto.response.PaginationInfo;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PageMapper {
    
    /**
     * Converts a Spring Page object to a PaginationInfo object
     * 
     * @param <T> The type of content in the page
     * @param page The Spring Page object to convert
     * @return A PaginationInfo object containing the page data
     */
    public <T> PaginationInfo<T> toPaginationInfo(Page<T> page) {
        return PaginationInfo.<T>builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }
}