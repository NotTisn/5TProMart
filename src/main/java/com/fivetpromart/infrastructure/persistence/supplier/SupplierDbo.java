package com.fivetpromart.infrastructure.persistence.supplier;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

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

    @Column(name = "supplied_product_type")
    String suppliedProductType;

    @Column(name = "current_debt", precision = 20, scale = 2)
    BigDecimal currentDebt;
}