package com.fivetpromart.application.port.out;

import com.fivetpromart.domain.model.statistics.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public interface IStatisticsPersistencePort {
    DashboardSummary getDashboardSummary(Instant startDate, Instant endDate);
    List<RevenueProfitData> getRevenueProfitData(Instant startDate, Instant endDate);
    List<OrderData> getOrderData(Instant startDate, Instant endDate);
    List<CategoryRevenue> getCategoryRevenue(Instant startDate, Instant endDate, Integer limit);
    List<TopSellingProduct> getTopSellingProducts(Instant startDate, Instant endDate, Integer limit);
}
