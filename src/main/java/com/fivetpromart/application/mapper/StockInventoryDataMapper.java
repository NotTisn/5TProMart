package com.fivetpromart.application.mapper;

import com.fivetpromart.application.dto.StockInventoryDto;
import com.fivetpromart.application.port.out.IProductRepository;
import com.fivetpromart.domain.model.Product;
import com.fivetpromart.domain.model.StockInventory;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class StockInventoryDataMapper {
    
    @Autowired
    protected IProductRepository productRepository;
    
    public StockInventoryDto toDto(StockInventory domain) {
        if (domain == null) {
            return null;
        }
        
        // Fetch product name
        String productName = null;
        if (domain.getProductId() != null) {
            productName = productRepository.findById(domain.getProductId())
                    .map(Product::getProductName)
                    .orElse(null);
        }
        
        return StockInventoryDto.builder()
                .lotId(domain.getLotId())
                .productId(domain.getProductId())
                .productName(productName)
                .manufactureDate(domain.getManufactureDate())
                .expirationDate(domain.getExpirationDate())
                .stockQuantity(domain.getStockQuantity())
                .quantityShelf(domain.getQuantityShelf())
                .quantityStorage(domain.getQuantityStorage())
                .importPrice(domain.getImportPrice())
                .status(domain.getStatus())
                .build();
    }
}
