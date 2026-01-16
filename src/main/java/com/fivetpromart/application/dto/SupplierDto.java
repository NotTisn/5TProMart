package com.fivetpromart.application.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class SupplierDto {
    private String supplierId;
    private String supplierName;
    private String address;
    private String phoneNumber;
    private String representName;
    private String representPhoneNumber;
    private String supplierType;
    private List<SuppliedProductDto> suppliedProducts;
    private BigDecimal currentDebt;
}
