package com.fivetpromart.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SupplierRequest {

    @NotBlank(message = "Supplier name is required.")
    private String supplierName;

    @NotBlank(message = "Address is required.")
    private String address;

    @NotBlank(message = "Phone number is required.")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must have 10 digits.")
    private String phoneNumber;

    // Optional fields - no @NotBlank validation
    private String representName;

    // Optional but validate format if provided
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must have 10 digits.")
    private String representPhoneNumber;

    @NotBlank(message = "Supplier type is required.")
    @Pattern(regexp = "^(Doanh nghiệp|Tư nhân)$", message = "Supplier type must be 'Doanh nghiệp' or 'Tư nhân'.")
    private String supplierType;

    @NotEmpty(message = "Supplied product type is required.")
    private List<String> suppliedProductType;

    // Note: currentDebt is NOT in request body - it's system-managed and defaults to 0
}