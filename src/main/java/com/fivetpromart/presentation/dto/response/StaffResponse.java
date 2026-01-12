package com.fivetpromart.presentation.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StaffResponse {
    private String profileId;
    private String userId;
    private String username;
    private String fullName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String accountType;
    private String avatarUrl;
    private String location;
    private String bio;
}
