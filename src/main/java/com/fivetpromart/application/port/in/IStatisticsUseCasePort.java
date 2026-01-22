package com.fivetpromart.application.port.in;

import com.fivetpromart.application.dto.*;
import com.fivetpromart.application.dto.query.GetStatisticsQuery;

import java.util.List;

public interface IStatisticsUseCasePort {
    DashboardSummaryDto getDashboardSummary(GetStatisticsQuery query);
    List<RevenueProfitDataDto> getRevenueProfitChart(GetStatisticsQuery query);
    List<OrderDataDto> getOrdersChart(GetStatisticsQuery query);
    List<CategoryRevenueDto> getCategoryRevenue(GetStatisticsQuery query);
    List<TopSellingProductDto> getTopSellingProducts(GetStatisticsQuery query);
}
