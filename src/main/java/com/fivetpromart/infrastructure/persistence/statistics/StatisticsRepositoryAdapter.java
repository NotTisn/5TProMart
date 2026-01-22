package com.fivetpromart.infrastructure.persistence.statistics;

import com.fivetpromart.application.port.out.IStatisticsPersistencePort;
import com.fivetpromart.domain.model.statistics.*;
import com.fivetpromart.infrastructure.persistence.customer.repository.ICustomerJpaRepository;
import com.fivetpromart.infrastructure.persistence.expense.repository.ExpenseJpaRepository;
import com.fivetpromart.infrastructure.persistence.jpa.repository.IOrderJpaRepository;
import com.fivetpromart.infrastructure.persistence.jpa.repository.IOrderItemJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class StatisticsRepositoryAdapter implements IStatisticsPersistencePort {

    private final IOrderJpaRepository orderRepository;
    private final IOrderItemJpaRepository orderItemRepository;
    private final ExpenseJpaRepository expenseRepository;
    private final ICustomerJpaRepository customerRepository;

    @Override
    public DashboardSummary getDashboardSummary(LocalDate startDate, LocalDate endDate) {
        log.info("Calculating dashboard summary from {} to {}", startDate, endDate);

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        // Total Revenue: Sum of totalAmount from completed orders
        BigDecimal totalRevenue = orderRepository.calculateTotalRevenue(startDateTime, endDateTime);
        if (totalRevenue == null) totalRevenue = BigDecimal.ZERO;

        // Total Expenses: Sum of amount from expenses in date range
        BigDecimal incurredStats = expenseRepository.calculateTotalExpenses(startDate, endDate);
        if (incurredStats == null) incurredStats = BigDecimal.ZERO;

        // Cost of Goods Sold: Sum of (quantity * importPrice) for sold items
        BigDecimal costOfGoodsSold = orderItemRepository.calculateCostOfGoodsSold(startDateTime, endDateTime);
        if (costOfGoodsSold == null) costOfGoodsSold = BigDecimal.ZERO;

        // Net Profit = Revenue - COGS - Expenses
        BigDecimal netProfit = totalRevenue.subtract(costOfGoodsSold).subtract(incurredStats);

        // Total Orders
        Integer totalOrders = orderRepository.countCompletedOrders(startDateTime, endDateTime);

        // Total Products Sold
        Integer totalProductsSold = orderItemRepository.sumQuantitySold(startDateTime, endDateTime);
        if (totalProductsSold == null) totalProductsSold = 0;

        // Average Order Value
        BigDecimal averageOrderValue = BigDecimal.ZERO;
        if (totalOrders != null && totalOrders > 0) {
            averageOrderValue = totalRevenue.divide(
                    BigDecimal.valueOf(totalOrders),
                    0,
                    RoundingMode.HALF_UP
            );
        }

        // Total Customers who made purchases in period
        Integer totalCustomers = orderRepository.countUniqueCustomers(startDateTime, endDateTime);

        // New Customers registered in period
        Integer newCustomers = customerRepository.countNewCustomers(startDateTime, endDateTime);

        return DashboardSummary.builder()
                .totalRevenue(totalRevenue)
                .netProfit(netProfit)
                .totalOrders(totalOrders != null ? totalOrders : 0)
                .totalProductsSold(totalProductsSold)
                .averageOrderValue(averageOrderValue)
                .totalCustomers(totalCustomers != null ? totalCustomers : 0)
                .newCustomers(newCustomers != null ? newCustomers : 0)
                .incurredStats(incurredStats)
                .build();
    }

    @Override
    public List<RevenueProfitData> getRevenueProfitData(LocalDate startDate, LocalDate endDate) {
        log.info("Getting revenue profit data from {} to {}", startDate, endDate);

        List<RevenueProfitData> result = new ArrayList<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            LocalDateTime dayStart = currentDate.atStartOfDay();
            LocalDateTime dayEnd = currentDate.atTime(LocalTime.MAX);

            // Revenue for the day
            BigDecimal revenue = orderRepository.calculateTotalRevenue(dayStart, dayEnd);
            if (revenue == null) revenue = BigDecimal.ZERO;

            // Expenses for the day
            BigDecimal expense = expenseRepository.calculateTotalExpenses(currentDate, currentDate);
            if (expense == null) expense = BigDecimal.ZERO;

            // COGS for the day
            BigDecimal cogs = orderItemRepository.calculateCostOfGoodsSold(dayStart, dayEnd);
            if (cogs == null) cogs = BigDecimal.ZERO;

            // Profit = Revenue - COGS - Expense
            BigDecimal profit = revenue.subtract(cogs).subtract(expense);

            result.add(RevenueProfitData.builder()
                    .date(currentDate)
                    .revenue(revenue)
                    .expense(expense)
                    .profit(profit)
                    .build());

            currentDate = currentDate.plusDays(1);
        }

        return result;
    }

    @Override
    public List<OrderData> getOrderData(LocalDate startDate, LocalDate endDate) {
        log.info("Getting order data from {} to {}", startDate, endDate);

        List<OrderData> result = new ArrayList<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            LocalDateTime dayStart = currentDate.atStartOfDay();
            LocalDateTime dayEnd = currentDate.atTime(LocalTime.MAX);

            Integer completedOrders = orderRepository.countCompletedOrders(dayStart, dayEnd);

            result.add(OrderData.builder()
                    .date(currentDate)
                    .completedOrders(completedOrders != null ? completedOrders : 0)
                    .build());

            currentDate = currentDate.plusDays(1);
        }

        return result;
    }

    @Override
    public List<CategoryRevenue> getCategoryRevenue(LocalDate startDate, LocalDate endDate, Integer limit) {
        log.info("Getting category revenue from {} to {} with limit {}", startDate, endDate, limit);

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<Object[]> rawData = orderItemRepository.getCategoryRevenue(startDateTime, endDateTime);

        List<CategoryRevenue> categoryList = rawData.stream()
                .map(row -> CategoryRevenue.builder()
                        .categoryId((String) row[0])
                        .categoryName((String) row[1])
                        .totalRevenue((BigDecimal) row[2])
                        .totalQuantitySold(((Number) row[3]).intValue())
                        .orderCount(((Number) row[4]).intValue())
                        .build())
                .sorted(Comparator.comparing(CategoryRevenue::getTotalRevenue).reversed())
                .collect(Collectors.toList());

        // If limit is set and we have more categories than limit
        if (limit != null && categoryList.size() > limit) {
            List<CategoryRevenue> topCategories = categoryList.subList(0, limit);
            List<CategoryRevenue> otherCategories = categoryList.subList(limit, categoryList.size());

            // Aggregate "Khác" category
            BigDecimal otherRevenue = otherCategories.stream()
                    .map(CategoryRevenue::getTotalRevenue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Integer otherQuantity = otherCategories.stream()
                    .map(CategoryRevenue::getTotalQuantitySold)
                    .reduce(0, Integer::sum);

            Integer otherOrderCount = otherCategories.stream()
                    .map(CategoryRevenue::getOrderCount)
                    .reduce(0, Integer::sum);

            CategoryRevenue otherCategory = CategoryRevenue.builder()
                    .categoryId("other")
                    .categoryName("Khác")
                    .totalRevenue(otherRevenue)
                    .totalQuantitySold(otherQuantity)
                    .orderCount(otherOrderCount)
                    .build();

            topCategories.add(otherCategory);
            return topCategories;
        }

        return categoryList;
    }

    @Override
    public List<TopSellingProduct> getTopSellingProducts(LocalDate startDate, LocalDate endDate, Integer limit) {
        log.info("Getting top selling products from {} to {} with limit {}", startDate, endDate, limit);

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<Object[]> rawData = orderItemRepository.getTopSellingProducts(startDateTime, endDateTime);

        return rawData.stream()
                .map(row -> TopSellingProduct.builder()
                        .productId((String) row[0])
                        .productName((String) row[1])
                        .categoryName((String) row[2])
                        .totalRevenue((BigDecimal) row[3])
                        .totalQuantitySold(((Number) row[4]).intValue())
                        .totalStockQuantity(((Number) row[5]).intValue())
                        .build())
                .sorted(Comparator.comparing(TopSellingProduct::getTotalQuantitySold).reversed())
                .limit(limit != null ? limit : 10)
                .collect(Collectors.toList());
    }
}
