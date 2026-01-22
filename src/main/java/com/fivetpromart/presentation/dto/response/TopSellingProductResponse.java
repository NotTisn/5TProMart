package com.fivetpromart.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class TopSellingProductResponse {
    private String productId;
    private String productName;
    private String categoryName;
    private BigDecimal totalRevenue;
    private Integer totalQuantitySold;
    private Integer totalStockQuantity;
}
