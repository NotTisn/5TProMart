package com.fivetpromart.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUserDto {
    
    private String userId;
    private String staffId;
    private String username;
    private String email;
    private LocalDate birthDate;
    private String location;
    private String fullName;
    private String phoneNumber;
    private List<String> roles;
    private Boolean authenticated;
}
