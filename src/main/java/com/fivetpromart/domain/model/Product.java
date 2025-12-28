package com.fivetpromart.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)public class Product {
    private String productId;
    private String productName;
    private String categoryId;
    private String unitOfMeasure;
    private BigDecimal sellingPrice;

    public static Product create(String productName, String categoryId, String unitOfMeasure, BigDecimal sellingPrice) {
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (categoryId == null || categoryId.isBlank()) {
            throw new IllegalArgumentException("Category ID cannot be empty");
        }
        if (sellingPrice != null && sellingPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Selling price cannot be negative");
        }

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
        if (categoryId != null && !categoryId.isBlank() && !Objects.equals(this.categoryId, categoryId))
            this.categoryId = categoryId;
        if (unitOfMeasure != null && !unitOfMeasure.isBlank())
            this.unitOfMeasure = unitOfMeasure;
        if (sellingPrice != null && sellingPrice.compareTo(BigDecimal.ZERO) >= 0)
            this.sellingPrice = sellingPrice;
    }
}
