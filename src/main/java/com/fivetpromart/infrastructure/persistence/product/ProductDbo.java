package com.fivetpromart.infrastructure.persistence.product;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Persistable; // Quan trọng để tối ưu insert

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDbo {

    @Id
    @Column(name = "product_id", length = 36)
    String productId;

    @Column(name = "product_name", nullable = false)
    String productName;

    @Column(name = "category_id", nullable = false, length = 36)
    String categoryId;

    @Column(name = "unit_of_measure")
    String unitOfMeasure;

    @Column(name = "selling_price", precision = 15, scale = 2)
    BigDecimal sellingPrice;

    @Column(name = "total_stock_quantity")
    private Long totalStockQuantity;

//    @Transient
//    @Builder.Default
//    boolean isNew = true;
//
//    @Override
//    public String getId() {
//        return productId;
//    }
//
//    @Override
//    public boolean isNew() {
//        return isNew;
//    }
//
//    @PostLoad
//    @PrePersist
//    void markNotNew() {
//        this.isNew = false;
//    }
}