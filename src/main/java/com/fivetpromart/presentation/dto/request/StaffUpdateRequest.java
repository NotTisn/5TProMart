package com.fivetpromart.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StaffUpdateRequest {

    private String fullName;

    @Email(message = "Email must be valid.")
    private String email;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must have 10 digits.")
    private String phoneNumber;

    @Pattern(regexp = "^(SalesStaff|WarehouseStaff)$", message = "Account type must be 'SalesStaff' or 'WarehouseStaff'.")
    private String accountType;

    private LocalDate dateOfBirth;

    private String location;

    private String bio;
}
