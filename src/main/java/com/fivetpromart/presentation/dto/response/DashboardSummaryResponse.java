package com.fivetpromart.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class DashboardSummaryResponse {
    private BigDecimal totalRevenue;
    private BigDecimal netProfit;
    private Integer totalOrders;
    private Integer totalProductsSold;
    private BigDecimal averageOrderValue;
    private Integer totalCustomers;
    private Integer newCustomers;
    private BigDecimal incurredStats;
}
