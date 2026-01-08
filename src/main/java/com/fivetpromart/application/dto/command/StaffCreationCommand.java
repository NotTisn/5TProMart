package com.fivetpromart.application.dto.command;

import lombok.Data;

@Data
public class StaffCreationCommand {
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String accountType; // "Admin" || "SalesStaff" || "WarehouseStaff"
    private String dateOfBirth;
}