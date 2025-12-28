package com.fivetpromart.application.port.in;

import com.fivetpromart.application.dto.StockInventoryDto;
import com.fivetpromart.application.dto.command.StockInventoryCreationCommand;

public interface IStockInventoryUseCasePort {
    StockInventoryDto createStockInventory(StockInventoryCreationCommand dto);
    StockInventoryDto updateStockInventory(StockInventoryDto dto);
    StockInventoryDto getStockInventoryById(String lotId);
    void deleteById(String lotId);
}
