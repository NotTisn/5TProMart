package com.fivetpromart.domain.model.statistics;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class CategoryRevenue {
    private String categoryId;
    private String categoryName;
    private BigDecimal totalRevenue;
    private Integer totalQuantitySold;
    private Integer orderCount;
}
