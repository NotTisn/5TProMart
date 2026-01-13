package com.fivetpromart.application.dto.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockInventorySearchQuery {
    private String search;  // lotId contains search string
    private String productId;  // filter by productId
    private String status;  // filter by status
    private String sortBy;  // expirationDate, stockQuantity, importPrice
    private String order;  // asc, desc
}
