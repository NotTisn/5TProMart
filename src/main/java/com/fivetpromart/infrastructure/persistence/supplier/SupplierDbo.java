package com.fivetpromart.infrastructure.persistence.supplier;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "suppliers")
public class SupplierDbo {

    @Id
    @Column(name = "supplier_id", length = 50)
    String supplierId;

    @Column(name = "supplier_name", nullable = false)
    String supplierName;

    @Column(name = "address", columnDefinition = "TEXT")
    String address;

    @Column(name = "phone_number", length = 20)
    String phoneNumber;

    @Column(name = "represent_name")
    String representName;

    @Column(name = "represent_phone_number", length = 20)
    String representPhoneNumber;

    @Column(name = "supplier_type")
    String supplierType;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "supplier_id")
    @Builder.Default
    List<SuppliedProductDbo> suppliedProducts = new ArrayList<>();

    @Column(name = "current_debt", precision = 20, scale = 2)
    BigDecimal currentDebt;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;
}