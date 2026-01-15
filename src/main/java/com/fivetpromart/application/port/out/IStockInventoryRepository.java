package com.fivetpromart.application.port.out;

import com.fivetpromart.application.dto.query.StockInventorySearchQuery;
import com.fivetpromart.domain.model.StockInventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IStockInventoryRepository {
    boolean existsById(String lotId);
    void deleteById(String lotId);
    StockInventory save(StockInventory model);
    Optional<StockInventory> findById(String lotId);
    List<StockInventory> searchStockInventories(StockInventorySearchQuery query);
    Page<StockInventory> searchStockInventories(StockInventorySearchQuery query, Pageable pageable);
}
