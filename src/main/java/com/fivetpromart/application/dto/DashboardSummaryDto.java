package com.fivetpromart.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class DashboardSummaryDto {
    private BigDecimal totalRevenue;
    private BigDecimal netProfit;
    private Integer totalOrders;
    private Integer totalProductsSold;
    private BigDecimal averageOrderValue;
    private Integer totalCustomers;
    private Integer newCustomers;
    private BigDecimal incurredStats;
}
