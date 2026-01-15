package com.fivetpromart.application.dto.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Query object for searching stock inventories
 * Used in GET /api/stock_inventories endpoint
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockInventorySearchQuery {
    
    /**
     * Search in lot_id (contains search string)
     */
    private String search;
    
    /**
     * Filter by product_id
     */
    private String productId;
    
    /**
     * Filter by status
     */
    private String status;
    
    /**
     * Sort by field: "expirationDate", "stockQuantity", "importPrice"
     */
    private String sortBy;
    
    /**
     * Sort order: "asc", "desc"
     */
    private String order;
}
