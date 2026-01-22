package com.fivetpromart.application.usecase;

import com.fivetpromart.application.dto.*;
import com.fivetpromart.application.dto.query.GetStatisticsQuery;
import com.fivetpromart.application.mapper.StatisticsDataMapper;
import com.fivetpromart.application.port.in.IStatisticsUseCasePort;
import com.fivetpromart.application.port.out.IStatisticsPersistencePort;
import com.fivetpromart.domain.model.statistics.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StatisticsUseCaseImpl implements IStatisticsUseCasePort {

    private final IStatisticsPersistencePort statisticsPersistencePort;
    private final StatisticsDataMapper mapper;

    @Override
    public DashboardSummaryDto getDashboardSummary(GetStatisticsQuery query) {
        log.info("Getting dashboard summary from {} to {}", query.getStartDate(), query.getEndDate());
        
        DashboardSummary summary = statisticsPersistencePort.getDashboardSummary(
                query.getStartDate(),
                query.getEndDate()
        );
        
        return mapper.toDto(summary);
    }

    @Override
    public List<RevenueProfitDataDto> getRevenueProfitChart(GetStatisticsQuery query) {
        log.info("Getting revenue profit chart from {} to {}", query.getStartDate(), query.getEndDate());
        
        List<RevenueProfitData> dataList = statisticsPersistencePort.getRevenueProfitData(
                query.getStartDate(),
                query.getEndDate()
        );
        
        return dataList.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDataDto> getOrdersChart(GetStatisticsQuery query) {
        log.info("Getting orders chart from {} to {}", query.getStartDate(), query.getEndDate());
        
        List<OrderData> dataList = statisticsPersistencePort.getOrderData(
                query.getStartDate(),
                query.getEndDate()
        );
        
        return dataList.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryRevenueDto> getCategoryRevenue(GetStatisticsQuery query) {
        log.info("Getting category revenue with limit {} from {} to {}", 
                query.getLimit(), query.getStartDate(), query.getEndDate());
        
        List<CategoryRevenue> dataList = statisticsPersistencePort.getCategoryRevenue(
                query.getStartDate(),
                query.getEndDate(),
                query.getLimit()
        );
        
        return dataList.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TopSellingProductDto> getTopSellingProducts(GetStatisticsQuery query) {
        log.info("Getting top selling products with limit {} from {} to {}", 
                query.getLimit(), query.getStartDate(), query.getEndDate());
        
        List<TopSellingProduct> dataList = statisticsPersistencePort.getTopSellingProducts(
                query.getStartDate(),
                query.getEndDate(),
                query.getLimit()
        );
        
        return dataList.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
