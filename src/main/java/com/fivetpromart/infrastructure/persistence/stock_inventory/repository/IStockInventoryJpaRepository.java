package com.fivetpromart.infrastructure.persistence.stock_inventory.repository;

import com.fivetpromart.infrastructure.persistence.stock_inventory.StockInventoryDbo;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface IStockInventoryJpaRepository extends
        JpaRepository<StockInventoryDbo,String>,
        JpaSpecificationExecutor<StockInventoryDbo> {

    /**
     * Find stock by lot ID with pessimistic write lock.
     * Use this for stock reservation to prevent race conditions.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM StockInventoryDbo s WHERE s.lotId = :lotId")
    Optional<StockInventoryDbo> findByIdForUpdate(@Param("lotId") String lotId);

    @Query("SELECT COALESCE(SUM(s.stockQuantity), 0) FROM StockInventoryDbo s WHERE s.productId = :productId")
    Long sumStockQuantityByProductId(@Param("productId") String productId);

    Long countByStockQuantityLessThan(Long threshold);

    Long countByStockQuantity(Long quantity);

    Long countByExpirationDateBefore(LocalDate date);

    Long countByExpirationDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT COALESCE(SUM(s.stockQuantity * s.importPrice), 0) FROM StockInventoryDbo s")
    BigDecimal calculateTotalInventoryValue();
}
