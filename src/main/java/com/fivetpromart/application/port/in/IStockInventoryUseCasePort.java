package com.fivetpromart.application.port.in;

import com.fivetpromart.application.dto.StockInventoryDto;
import com.fivetpromart.application.dto.command.StockInventoryCreationCommand;
import com.fivetpromart.application.dto.command.StockInventoryUpdateCommand;
import com.fivetpromart.application.dto.query.StockInventorySearchQuery;

import java.util.List;

public interface IStockInventoryUseCasePort {
    /**
     * Get all stock inventories with search and filters
     */
//    List<StockInventoryDto> searchStockInventories(StockInventorySearchQuery query);
//
//    /**
//     * Get stock inventory by ID
//     */
//    StockInventoryDto getStockInventoryById(String lotId);

    /**
     * Create new stock inventory
     */
    StockInventoryDto createStockInventory(StockInventoryCreationCommand command);

    /**
     * Update stock inventory
     */
    StockInventoryDto updateStockInventory(String lotId, StockInventoryUpdateCommand command);

    /**
     * Delete stock inventory
     */
//    void deleteById(String lotId);
}
