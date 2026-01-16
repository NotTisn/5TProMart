package com.fivetpromart.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuppliedProduct {
    private String productId;
    private BigDecimal lastImportPrice;
    private LocalDate lastImportDate;

    /**
     * Create a new supplied product with default values
     */
    public static SuppliedProduct createNew(String productId) {
        return SuppliedProduct.builder()
                .productId(productId)
                .lastImportPrice(BigDecimal.ZERO)
                .lastImportDate(null)
                .build();
    }

    /**
     * Update import information
     */
    public void updateImportInfo(BigDecimal importPrice, LocalDate importDate) {
        this.lastImportPrice = importPrice;
        this.lastImportDate = importDate;
    }
}
