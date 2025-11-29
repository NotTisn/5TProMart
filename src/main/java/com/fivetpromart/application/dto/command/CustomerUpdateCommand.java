package com.fivetpromart.application.dto.command;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class CustomerUpdateCommand {
    private String customerId;
    private String fullName;
    private String gender;
    private LocalDate dateOfBirth;
    private String phoneNumber;
}
