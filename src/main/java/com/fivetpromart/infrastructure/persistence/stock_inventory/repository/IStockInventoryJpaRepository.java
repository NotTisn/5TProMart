package com.fivetpromart.infrastructure.persistence.stock_inventory.repository;

import com.fivetpromart.infrastructure.persistence.stock_inventory.StockInventoryDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface IStockInventoryJpaRepository extends
        JpaRepository<StockInventoryDbo,String>,
        JpaSpecificationExecutor<StockInventoryDbo> {

    @Query("SELECT COALESCE(SUM(s.stockQuantity), 0) FROM StockInventoryDbo s WHERE s.productId = :productId")
    Long sumStockQuantityByProductId(@Param("productId") String productId);

    Long countByStockQuantityLessThan(Long threshold);

    Long countByStockQuantity(Long quantity);

    Long countByExpirationDateBefore(LocalDate date);

    Long countByExpirationDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT COALESCE(SUM(s.stockQuantity * s.importPrice), 0) FROM StockInventoryDbo s")
    BigDecimal calculateTotalInventoryValue();
}
