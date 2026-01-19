package com.fivetpromart.application.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkScheduleDto {
    
    String id;
    LocalDate workDate;
    String workShiftId;
    String shiftName;
    LocalTime startTime;
    LocalTime endTime;
    
    boolean isCompliant;
    
    @Builder.Default
    List<RoleRequirementDto> requirements = new ArrayList<>();
    
    @Builder.Default
    List<RoleRequirementDto> missingRoles = new ArrayList<>();
    
    @Builder.Default
    List<StaffAssignmentDto> assignments = new ArrayList<>();
    
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class RoleRequirementDto {
        String accountType;
        int quantity;
    }
    
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class StaffAssignmentDto {
        String profileId;
        String fullName;
        String accountType;
        String email;
        String phoneNumber;
        String status;
    }
}
