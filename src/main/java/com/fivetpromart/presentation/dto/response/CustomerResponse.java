package com.fivetpromart.presentation.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class CustomerResponse {
    private String customerId;
    private String fullName;
    private String gender;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private LocalDate registrationDate;
    private long loyaltyPoints;
}
