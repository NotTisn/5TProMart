package com.fivetpromart.application.dto.query;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CustomerSearchQuery {
    private String customerId;
    private String customerName;
    private String phoneNumber; // Exact phone number match for POS lookup
    
    // Soft delete filter
    private Boolean includeDeleted; // null or false = only active, true = include deleted
}
