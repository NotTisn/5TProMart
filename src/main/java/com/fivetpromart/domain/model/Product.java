package com.fivetpromart.domain.model;

import com.fivetpromart.infrastructure.error.AppException;
import com.fivetpromart.infrastructure.error.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class Product {
    private String productId;
    private String productName;
    private String categoryId;
    private String unitOfMeasure;
    private BigDecimal sellingPrice;

    public static Product createProduct(String productName, String categoryId, String unitOfMeasure, BigDecimal sellingPrice) {
        if(productName == null || productName.isBlank())
            throw new AppException(ErrorCode.CANNOT_BE_EMPTY);
        if(categoryId == null || categoryId.isBlank())
            throw new AppException(ErrorCode.CANNOT_BE_EMPTY);

        Product product = new Product();
        product.productId = UUID.randomUUID().toString();
        product.productName = productName;
        product.categoryId = categoryId;
        product.unitOfMeasure = unitOfMeasure;
        product.sellingPrice = sellingPrice;

        return product;
    }

    public static Product reconstitute (
            String productId,
            String productName,
            String categoryId,
            String unitOfMeasure,
            BigDecimal sellingPrice
    ) {
        Product product = new Product();
        product.productId = productId;
        product.productName = productName;
        product.categoryId = categoryId;
        product.unitOfMeasure = unitOfMeasure;
        product.sellingPrice = sellingPrice;
        return product;
    }

    public void updateProduct(String productName, String categoryId, String unitOfMeasure, BigDecimal sellingPrice) {
        if (productName != null && !productName.isBlank())
            this.productName = productName;
        if (categoryId != null && !categoryId.isBlank())
            this.categoryId = categoryId;
        if (unitOfMeasure != null && !unitOfMeasure.isBlank())
            this.unitOfMeasure = unitOfMeasure;
        if (sellingPrice != null && sellingPrice.compareTo(BigDecimal.ZERO) >= 0)
            this.sellingPrice = sellingPrice;
    }
}
