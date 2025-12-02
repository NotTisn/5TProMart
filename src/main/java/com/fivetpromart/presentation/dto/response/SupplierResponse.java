package com.fivetpromart.presentation.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SupplierResponse {
    private String supplierId;
    private String supplierName;
    private String supplierType;
    private String phoneNumber;
    private String address;
    private String suppliedProductType;
    private BigDecimal currentDebt;
}
