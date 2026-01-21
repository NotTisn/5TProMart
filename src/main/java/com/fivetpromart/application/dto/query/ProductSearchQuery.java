package com.fivetpromart.application.dto.query;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductSearchQuery {
    // Filter fields
    private String productId;
    private String categoryId;
    private String productName;
    
    // Soft delete filter
    private Boolean includeDeleted; // null or false = only active, true = include deleted
}