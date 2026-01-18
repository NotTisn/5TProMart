package com.fivetpromart.infrastructure.persistence.stock_inventory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_inventories")
@EntityListeners(AuditingEntityListener.class) // NEW: Enable JPA auditing
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StockInventoryDbo {
    @Id
    @Column(name = "lot_id")
    String lotId;

    @Column(name = "product_id")
    String productId;

    @Column(name = "manufacture_date")
    LocalDate manufactureDate;

    @Column(name = "expiration_date")
    LocalDate expirationDate;

    @Column(name = "stock_quantity")
    Long stockQuantity;

    @Column(name = "reserved_quantity")
    Long reservedQuantity; // NEW: Track reserved stock

    @Column(name = "import_price")
    BigDecimal importPrice;

    @Column(name = "status")
    String status;
    
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
    // OPTIMISTIC LOCKING
    // ============================================================================
    
    @Version
    @Column(name = "version")
    Long version;
}
