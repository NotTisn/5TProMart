package com.fivetpromart.infrastructure.persistence.product;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable; // Quan trọng để tối ưu insert
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@EntityListeners(AuditingEntityListener.class) // NEW: Enable JPA auditing
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
    // ============================================================================
    // AUDIT FIELDS (NEW)
    // ============================================================================
    
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    String createdBy;
    
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;
    
    @LastModifiedBy
    @Column(name = "updated_by")
    String updatedBy;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    // ============================================================================
    // SOFT DELETE FIELD (UPDATED)
    // ============================================================================
    
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    Boolean isActive = true;

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