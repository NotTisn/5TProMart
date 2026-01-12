package com.fivetpromart.application.dto.command;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class SupplierUpdateCommand {
    private String supplierId;
    private String supplierName;
    private String address;
    private String phoneNumber;
    private String representName;
    private String representPhoneNumber;
    private String supplierType;
    private String suppliedProductType;
    // Note: currentDebt is NOT updated via this command - it's managed separately through business operations
}
