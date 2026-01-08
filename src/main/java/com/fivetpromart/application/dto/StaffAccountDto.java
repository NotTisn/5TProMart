package com.fivetpromart.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StaffAccountDto {
    private String profileId;
    private String username;
    private String fullName;
    private String accountType;
}