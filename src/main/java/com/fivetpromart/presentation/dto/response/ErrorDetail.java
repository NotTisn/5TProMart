package com.fivetpromart.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * ErrorDetail - Structured error information for API responses
 * 
 * Provides machine-readable error codes and human-readable details.
 * Enables frontend to handle errors programmatically (e.g., i18n, conditional logic).
 * 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDetail {
    
    /**
     * Machine-readable error code (e.g., "CUSTOMER_NOT_FOUND", "INVALID_PHONE_NUMBER")
     * Frontend can use this for conditional logic or i18n mapping
     */
    String code;
    
    /**
     * Additional structured details about the error (e.g., field validation errors)
     */
    Object details;
    
    /**
     * Create error detail with code only
     */
    public static ErrorDetail of(String code) {
        return ErrorDetail.builder()
                .code(code)
                .build();
    }
    
    /**
     * Create error detail with code and details
     */
    public static ErrorDetail of(String code, Object details) {
        return ErrorDetail.builder()
                .code(code)
                .details(details)
                .build();
    }
}
