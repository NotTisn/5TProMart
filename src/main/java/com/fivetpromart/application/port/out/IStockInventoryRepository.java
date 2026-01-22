package com.fivetpromart.application.port.out;

import com.fivetpromart.application.dto.query.StockInventorySearchQuery;
import com.fivetpromart.domain.model.StockInventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IStockInventoryRepository {
    boolean existsById(String lotId);
    void deleteById(String lotId);
    StockInventory save(StockInventory model);
    Optional<StockInventory> findById(String lotId);
    
    /**
     * Find stock by lot ID with pessimistic write lock.
     * Use this for stock reservation to prevent race conditions.
     */
    Optional<StockInventory> findByIdForUpdate(String lotId);
    
    List<StockInventory> searchStockInventories(StockInventorySearchQuery query);
    Page<StockInventory> searchStockInventories(StockInventorySearchQuery query, Pageable pageable);
    
    // Stats methods
    Long getTotalStockByProductId(String productId);
    Long countByStockQuantityLessThan(Long threshold);
    Long countByStockQuantityEquals(Long quantity);
    Long countByExpirationDateBefore(LocalDate date);
    Long countByExpirationDateBetween(LocalDate startDate, LocalDate endDate);
    BigDecimal calculateTotalInventoryValue();
    
    /**
     * Find lots where expiration date has passed but status is still AVAILABLE.
     * Used by the auto-expire scheduler to mark them as EXPIRED.
     */
    List<StockInventory> findExpiredButNotMarked(LocalDate today);
    
    /**
     * Batch save multiple stock inventories.
     */
    void saveAll(List<StockInventory> inventories);
}
