package com.fivetpromart.presentation.mapper;

import com.fivetpromart.application.dto.*;
import com.fivetpromart.application.dto.query.GetStatisticsQuery;
import com.fivetpromart.presentation.dto.response.*;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StatisticsPresentationMapper {
    
    DashboardSummaryResponse toResponse(DashboardSummaryDto dto);
    
    RevenueProfitDataResponse toResponse(RevenueProfitDataDto dto);
    
    OrderDataResponse toResponse(OrderDataDto dto);
    
    CategoryRevenueResponse toResponse(CategoryRevenueDto dto);
    
    TopSellingProductResponse toResponse(TopSellingProductDto dto);
}
