package com.fivetpromart.application.dto.command;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class StaffUpdateCommand {
    private String staffId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String accountType;
    private LocalDate dateOfBirth;
    private String location;
    private String bio;
}
