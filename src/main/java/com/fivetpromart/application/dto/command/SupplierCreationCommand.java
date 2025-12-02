package com.fivetpromart.application.dto.command;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder(toBuilder = true)
public class SupplierCreationCommand {
    private String supplierName;
    private String supplierType;
    private String phoneNumber;
    private String address;
    private String suppliedProductType;
}
