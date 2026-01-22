package com.fivetpromart.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class StockInventoryResponse {
    private String lotId;
    private String productId;
    private String productName;
    private LocalDate manufactureDate;
    private LocalDate expirationDate;
    private Long stockQuantity;
    private Long quantityShelf;     // Display quantity (items on shelf)
    private Long quantityStorage;   // Warehouse quantity (items in storage)
    private BigDecimal importPrice;
    private String status;
}
