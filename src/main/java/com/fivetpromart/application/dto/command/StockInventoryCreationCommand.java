package com.fivetpromart.application.dto.command;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class StockInventoryCreationCommand {
    private String productId;
    private LocalDate manufactureDate;
    private LocalDate expirationDate;
    private Long stockQuantity;
    private BigDecimal importPrice;
}
