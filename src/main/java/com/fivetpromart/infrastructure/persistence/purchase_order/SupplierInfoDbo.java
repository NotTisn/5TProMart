package com.fivetpromart.infrastructure.persistence.purchase_order;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SupplierInfoDbo {

    @Column(name = "supplier_id")
    String supplierId;

    @Column(name = "supplier_name")
    String supplierName;

    @Column(name = "supplier_phone")
    String phone;

    @Column(name = "supplier_represent_name")
    String representName;

    @Column(name = "supplier_represent_phone")
    String representPhoneNumber;
}
