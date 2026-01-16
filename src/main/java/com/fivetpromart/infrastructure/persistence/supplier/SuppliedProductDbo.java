package com.fivetpromart.infrastructure.persistence.supplier;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "supplied_products")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SuppliedProductDbo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    
    @Column(name = "supplier_id", nullable = false)
    String supplierId;
    
    @Column(name = "product_id", nullable = false)
    String productId;
    
    @Column(name = "last_import_price")
    BigDecimal lastImportPrice;
    
    @Column(name = "last_import_date")
    LocalDate lastImportDate;
}
