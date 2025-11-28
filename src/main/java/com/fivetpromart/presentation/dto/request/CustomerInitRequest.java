package com.fivetpromart.presentation.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CustomerInitRequest {
    private String fullName;
    private String gender;
    private LocalDate dateOfBirth;
    private String phoneNumber;
}
