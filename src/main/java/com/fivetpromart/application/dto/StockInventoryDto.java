package com.fivetpromart.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class StockInventoryDto {
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
