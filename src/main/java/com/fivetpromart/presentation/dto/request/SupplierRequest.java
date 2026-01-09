package com.fivetpromart.presentation.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SupplierRequest {

    @NotBlank(message = "Supplier name is required")
    @Size(max = 255, message = "Supplier name must be less than 255 characters")
    private String supplierName;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Phone number is required")
    // Simple regex for 10-15 digits, optional + prefix
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number format")
    private String phoneNumber;

    @NotBlank(message = "Representative name is required")
    private String representName;

    @NotBlank(message = "Representative phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid representative phone number format")
    private String representPhoneNumber;

    @NotBlank(message = "Supplier type is required")
    private String supplierType;

    @NotBlank(message = "Supplied product type is required")
    private String suppliedProductType;

    @NotNull(message = "Current debt is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Current debt cannot be negative")
    private BigDecimal currentDebt;
}