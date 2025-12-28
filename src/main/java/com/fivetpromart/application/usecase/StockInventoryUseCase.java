package com.fivetpromart.application.usecase;

import com.fivetpromart.application.dto.StockInventoryDto;
import com.fivetpromart.application.dto.command.StockInventoryCreationCommand;
import com.fivetpromart.application.port.in.IStockInventoryUseCasePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockInventoryUseCase implements IStockInventoryUseCasePort {
    @Override
    public StockInventoryDto createStockInventory(StockInventoryCreationCommand dto) {
        return null;
    }

    @Override
    public StockInventoryDto updateStockInventory(StockInventoryDto dto) {
        return null;
    }

    @Override
    public StockInventoryDto getStockInventoryById(String lotId) {
        return null;
    }

    @Override
    public void deleteById(String lotId) {

    }
}
