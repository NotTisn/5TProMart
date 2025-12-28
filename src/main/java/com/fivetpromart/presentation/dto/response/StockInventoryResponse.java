package com.fivetpromart.presentation.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class StockInventoryResponse {
    private String productId;
    private LocalDate manufactureDate;
    private LocalDate expirationDate;
    private Long stockQuantity;
    private BigDecimal importPrice;
    private String status;
}
