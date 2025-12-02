package com.fivetpromart.infrastructure.persistence.product.mapper;

import com.fivetpromart.domain.model.Product;
import com.fivetpromart.infrastructure.persistence.product.ProductDbo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductPersistenceMapper {

    default ProductDbo toDbo(Product domain) {
        if (domain == null) return null;

        return ProductDbo.builder()
                .productId(domain.getProductId())
                .productName(domain.getProductName())
                .categoryId(domain.getCategoryId())
                .unitOfMeasure(domain.getUnitOfMeasure())
                .sellingPrice(domain.getSellingPrice())
                .build();
    }

    default Product toDomain(ProductDbo dbo) {
        if (dbo == null) return null;

        return Product.reconstitute(
                dbo.getProductId(),
                dbo.getProductName(),
                dbo.getCategoryId(),
                dbo.getUnitOfMeasure(),
                dbo.getSellingPrice()
        );
    }
}