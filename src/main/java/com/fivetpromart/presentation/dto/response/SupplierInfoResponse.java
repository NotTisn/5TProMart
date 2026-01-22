package com.fivetpromart.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SupplierInfoResponse {
    private String supplierId;
    private String supplierName;
    private String phone;
    private String representName;
    private String representPhoneNumber;
}
