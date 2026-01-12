package com.fivetpromart.application.dto.query;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SupplierSearchQuery {
    private String supplierId;
    private String supplierName;
    private String supplierType;
}
