package com.fivetpromart.infrastructure.persistence.supplier_product;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(
        name = "supplier_products",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_supplier_product", columnNames = {"supplier_id", "product_id"})
        }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SupplierProductDbo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    String id;

    @Column(name = "supplier_id", nullable = false)
    String supplierId;

    @Column(name = "product_id", nullable = false)
    String productId;
}