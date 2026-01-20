package com.fivetpromart.application.mapper;

import com.fivetpromart.application.dto.*;
import com.fivetpromart.domain.model.statistics.*;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StatisticsDataMapper {
    DashboardSummaryDto toDto(DashboardSummary dashboardSummary);
    RevenueProfitDataDto toDto(RevenueProfitData revenueProfitData);
    OrderDataDto toDto(OrderData orderData);
    CategoryRevenueDto toDto(CategoryRevenue categoryRevenue);
    TopSellingProductDto toDto(TopSellingProduct topSellingProduct);
}
