package com.fivetpromart.application.dto.command;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CustomerCreationCommand {
    private String fullName;
    private String gender;
    private LocalDate dateOfBirth;
    private String phoneNumber;
}
