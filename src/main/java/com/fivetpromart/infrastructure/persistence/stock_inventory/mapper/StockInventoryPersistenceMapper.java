package com.fivetpromart.infrastructure.persistence.stock_inventory.mapper;

import com.fivetpromart.domain.model.StockInventory;
import com.fivetpromart.infrastructure.persistence.stock_inventory.StockInventoryDbo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockInventoryPersistenceMapper {
    default StockInventoryDbo toDbo(StockInventory stockInventory) {
        if (stockInventory == null) return null;

        // Convert enum to string for persistence
        String statusValue = stockInventory.getStatus() != null ? stockInventory.getStatus().getValue() : "AVAILABLE";

        return StockInventoryDbo.builder()
                .lotId(stockInventory.getLotId())
                .productId(stockInventory.getProductId())
                .expirationDate(stockInventory.getExpirationDate())
                .manufactureDate(stockInventory.getManufactureDate())
                .stockQuantity(stockInventory.getStockQuantity())
                .reservedQuantity(stockInventory.getReservedQuantity())
                .quantityShelf(stockInventory.getQuantityShelf())
                .quantityStorage(stockInventory.getQuantityStorage())
                .importPrice(stockInventory.getImportPrice())
                .status(statusValue)
                .build();
    }

    default StockInventory toDomain(StockInventoryDbo stockInventoryDbo) {
        if (stockInventoryDbo == null) return null;

        return StockInventory.reconstitute(
                stockInventoryDbo.getLotId(),
                stockInventoryDbo.getProductId(),
                stockInventoryDbo.getManufactureDate(),
                stockInventoryDbo.getExpirationDate(),
                stockInventoryDbo.getStockQuantity(),
                stockInventoryDbo.getReservedQuantity(),
                stockInventoryDbo.getQuantityShelf(),
                stockInventoryDbo.getQuantityStorage(),
                stockInventoryDbo.getImportPrice(),
                stockInventoryDbo.getStatus()
        );
    }
}
