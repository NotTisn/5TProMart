package com.fivetpromart.application.port.out;

import com.fivetpromart.domain.model.StockInventory;

import java.util.Optional;

public interface IStockInventoryRepository {
    boolean existsById(String lotId);
    void deleteById(String lotId);
    StockInventory save(StockInventory model);
    Optional<StockInventory> findById(String lotId);
}
