package com.fivetpromart.application.port.out;

import com.fivetpromart.domain.model.statistics.*;

import java.time.LocalDate;
import java.util.List;

public interface IStatisticsPersistencePort {
    DashboardSummary getDashboardSummary(LocalDate startDate, LocalDate endDate);
    List<RevenueProfitData> getRevenueProfitData(LocalDate startDate, LocalDate endDate);
    List<OrderData> getOrderData(LocalDate startDate, LocalDate endDate);
    List<CategoryRevenue> getCategoryRevenue(LocalDate startDate, LocalDate endDate, Integer limit);
    List<TopSellingProduct> getTopSellingProducts(LocalDate startDate, LocalDate endDate, Integer limit);
}
