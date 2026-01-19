package com.fivetpromart.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductStatsDto {
    private Long totalProducts;
    private Long activeProducts;
    private Long inactiveProducts;
    private BigDecimal totalInventoryValue;
    private Long lowStockCount;
    private Long outOfStockCount;
    private Long expiringSoonCount;
    private Long expiredCount;
}
