package com.fivetpromart.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierProductResponse {
    private String productId;
    private String productName;
    private String unitOfMeasure;
    private Integer totalStockQuantity;
    private BigDecimal lastImportPrice;
    private LocalDate lastImportDate;
}
