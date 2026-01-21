package com.fivetpromart.domain.model;

import com.fivetpromart.domain.exception.EmptyFieldException;
import com.fivetpromart.domain.exception.InvalidPriceException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {
    private String productId;
    private String productName;
    private String categoryId;
    private String unitOfMeasure;
    private BigDecimal sellingPrice;
    private Integer totalStockQuantity;
    private Boolean isActive = true;

    public static Product create(String productName, String categoryId, String unitOfMeasure, BigDecimal sellingPrice) {
        if (productName == null || productName.isBlank()) {
            throw new EmptyFieldException("Product name");
        }
        if (categoryId == null || categoryId.isBlank()) {
            throw new EmptyFieldException("Category ID");
        }
        if (sellingPrice != null && sellingPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidPriceException("Selling price cannot be negative");
        }

        Product product = new Product();
        product.productId = UUID.randomUUID().toString();
        product.productName = productName;
        product.categoryId = categoryId;
        product.unitOfMeasure = unitOfMeasure;
        product.sellingPrice = sellingPrice;
        product.totalStockQuantity = 0;

        return product;
    }

    public static Product reconstitute(
            String productId,
            String productName,
            String categoryId,
            String unitOfMeasure,
            BigDecimal sellingPrice,
            Integer totalStockQuantity
    ) {
        Product product = new Product();
        product.productId = productId;
        product.productName = productName;
        product.categoryId = categoryId;
        product.unitOfMeasure = unitOfMeasure;
        product.sellingPrice = sellingPrice;
        product.totalStockQuantity = totalStockQuantity;
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

    public void updateTotalStockQuantity(Integer quantity) {
        if (quantity != null && quantity >= 0) {
            this.totalStockQuantity = quantity;
        }
    }

    // Soft delete methods
    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }

    public boolean isActive() {
        return this.isActive != null && this.isActive;
    }
}