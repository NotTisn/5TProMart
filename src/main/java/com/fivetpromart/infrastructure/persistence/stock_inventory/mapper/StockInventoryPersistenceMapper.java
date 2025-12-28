package com.fivetpromart.infrastructure.persistence.stock_inventory.mapper;

import com.fivetpromart.domain.model.StockInventory;
import com.fivetpromart.infrastructure.persistence.stock_inventory.StockInventoryDbo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockInventoryPersistenceMapper {
    default StockInventoryDbo toDbo(StockInventory stockInventory) {
        if (stockInventory == null) return null;

        return StockInventoryDbo.builder()
                .lotId(stockInventory.getLotId())
                .productId(stockInventory.getProductId())
                .expirationDate(stockInventory.getExpirationDate())
                .manufactureDate(stockInventory.getManufactureDate())
                .stockQuantity(stockInventory.getStockQuantity())
                .importPrice(stockInventory.getImportPrice())
                .status(stockInventory.getStatus())
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
                stockInventoryDbo.getImportPrice(),
                stockInventoryDbo.getStatus()
        );
    }
}
