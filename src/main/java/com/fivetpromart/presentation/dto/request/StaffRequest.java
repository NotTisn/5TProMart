package com.fivetpromart.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StaffRequest {

    @NotBlank(message = "Username is required.")
    @Pattern(regexp = "^[a-zA-Z0-9_]{3,50}$", message = "Username must be 3-50 characters and contain only letters, numbers, and underscores.")
    private String username;

    @NotBlank(message = "Password is required.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$", message = "Password must be at least 8 characters with uppercase, lowercase, and number.")
    private String password;

    @NotBlank(message = "Full name is required.")
    private String fullName;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email must be valid.")
    private String email;

    @NotBlank(message = "Phone number is required.")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must have 10 digits.")
    private String phoneNumber;

    @NotBlank(message = "Account type is required.")
    @Pattern(regexp = "^(SalesStaff|WarehouseStaff)$", message = "Account type must be 'SalesStaff' or 'WarehouseStaff'.")
    private String accountType;

    private LocalDate dateOfBirth;

    private String location;

    private String bio;
}
