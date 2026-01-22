package com.fivetpromart.presentation.controller;

import com.fivetpromart.application.dto.*;
import com.fivetpromart.application.dto.query.GetStatisticsQuery;
import com.fivetpromart.application.port.in.IStatisticsUseCasePort;
import com.fivetpromart.presentation.dto.response.*;
import com.fivetpromart.presentation.mapper.StatisticsPresentationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
@Slf4j
public class StatisticsController {

    private final IStatisticsUseCasePort statisticsUseCase;
    private final StatisticsPresentationMapper mapper;

    /**
     * 1.1 Get Dashboard Summary
     * GET /api/v1/statistics/summary
     */
    @GetMapping("/summary")
    public ApiResponse<DashboardSummaryResponse> getDashboardSummary(
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Instant startDate,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Instant endDate
    ) {
        log.info("Getting dashboard summary from {} to {}", startDate, endDate);

        GetStatisticsQuery query = GetStatisticsQuery.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();

        DashboardSummaryDto dto = statisticsUseCase.getDashboardSummary(query);
        DashboardSummaryResponse response = mapper.toResponse(dto);

        return ApiResponse.<DashboardSummaryResponse>builder()
                .success(true)
                .message("Get summary successfully.")
                .data(response)
                .build();
    }

    /**
     * 1.2 Get Revenue & Profit Chart
     * GET /api/v1/statistics/revenue-profit-chart
     */
    @GetMapping("/revenue-profit-chart")
    public ApiResponse<List<RevenueProfitDataResponse>> getRevenueProfitChart(
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Instant startDate,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Instant endDate
    ) {
        log.info("Getting revenue profit chart from {} to {}", startDate, endDate);

        GetStatisticsQuery query = GetStatisticsQuery.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();

        List<RevenueProfitDataDto> dtos = statisticsUseCase.getRevenueProfitChart(query);
        List<RevenueProfitDataResponse> responses = dtos.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        return ApiResponse.<List<RevenueProfitDataResponse>>builder()
                .success(true)
                .message("Get revenue profit chart successfully.")
                .data(responses)
                .build();
    }

    /**
     * 1.3 Get Order By Days Chart
     * GET /api/v1/statistics/orders-chart
     */
    @GetMapping("/orders-chart")
    public ApiResponse<List<OrderDataResponse>> getOrdersChart(
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Instant startDate,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Instant endDate
    ) {
        log.info("Getting orders chart from {} to {}", startDate, endDate);

        GetStatisticsQuery query = GetStatisticsQuery.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();

        List<OrderDataDto> dtos = statisticsUseCase.getOrdersChart(query);
        List<OrderDataResponse> responses = dtos.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        return ApiResponse.<List<OrderDataResponse>>builder()
                .success(true)
                .message("Get orders chart successfully.")
                .data(responses)
                .build();
    }

    /**
     * 1.4 Get Revenue by Category
     * GET /api/v1/statistics/category-revenue
     */
    @GetMapping("/category-revenue")
    public ApiResponse<List<CategoryRevenueResponse>> getCategoryRevenue(
            @RequestParam(required = false, defaultValue = "5") Integer limit,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Instant startDate,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Instant endDate
    ) {
        log.info("Getting category revenue with limit {} from {} to {}", limit, startDate, endDate);

        GetStatisticsQuery query = GetStatisticsQuery.builder()
                .startDate(startDate)
                .endDate(endDate)
                .limit(limit)
                .build();

        List<CategoryRevenueDto> dtos = statisticsUseCase.getCategoryRevenue(query);
        List<CategoryRevenueResponse> responses = dtos.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        return ApiResponse.<List<CategoryRevenueResponse>>builder()
                .success(true)
                .message("Get category revenue successfully.")
                .data(responses)
                .build();
    }

    /**
     * 1.5 Get Top Selling Products
     * GET /api/v1/statistics/top-products
     */
    @GetMapping("/top-products")
    public ApiResponse<List<TopSellingProductResponse>> getTopSellingProducts(
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Instant startDate,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Instant endDate
    ) {
        log.info("Getting top selling products with limit {} from {} to {}", limit, startDate, endDate);

        GetStatisticsQuery query = GetStatisticsQuery.builder()
                .startDate(startDate)
                .endDate(endDate)
                .limit(limit)
                .build();

        List<TopSellingProductDto> dtos = statisticsUseCase.getTopSellingProducts(query);
        List<TopSellingProductResponse> responses = dtos.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        return ApiResponse.<List<TopSellingProductResponse>>builder()
                .success(true)
                .message("Get top selling products successfully.")
                .data(responses)
                .build();
    }
}
