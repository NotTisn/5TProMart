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
    
    /**
     * Stock level filter for stats drill-down.
     * Values: "low" (below threshold), "out" (zero stock), 
     * "expiring-soon" (within 7 days), "expired" (past expiry)
     */
    private String stockLevel;
}