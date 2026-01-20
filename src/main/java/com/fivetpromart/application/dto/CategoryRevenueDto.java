package com.fivetpromart.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class CategoryRevenueDto {
    private String categoryId;
    private String categoryName;
    private BigDecimal totalRevenue;
    private Integer totalQuantitySold;
    private Integer orderCount;
}
