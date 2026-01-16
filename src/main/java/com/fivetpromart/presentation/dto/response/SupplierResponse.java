package com.fivetpromart.presentation.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class SupplierResponse {
    private String supplierId;
    private String supplierName;
    private String address;
    private String phoneNumber;
    private String representName;
    private String representPhoneNumber;
    private String supplierType;
    private List<SuppliedProductResponse> suppliedProducts;
    private BigDecimal currentDebt;
}
