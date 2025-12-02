package com.fivetpromart.application.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder(toBuilder = true)
public class SupplierDto {
    private String supplierId;
    private String supplierName;
    private String supplierType;
    private String phoneNumber;
    private String address;
    private String suppliedProductType;
    private BigDecimal currentDebt;
}
