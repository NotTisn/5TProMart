package com.fivetpromart.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkScheduleResponse {
    
    @JsonProperty("id")
    String id;
    
    @JsonFormat(pattern = "dd-MM-yyyy")
    @JsonProperty("workDate")
    LocalDate workDate;
    
    @JsonProperty("workShiftId")
    String workShiftId;
    
    @JsonProperty("shiftName")
    String shiftName;
    
    @JsonFormat(pattern = "HH:mm")
    @JsonProperty("startTime")
    LocalTime startTime;
    
    @JsonFormat(pattern = "HH:mm")
    @JsonProperty("endTime")
    LocalTime endTime;
    
    @JsonProperty("isCompliant")
    boolean isCompliant;
    
    @JsonProperty("missingRoles")
    List<RoleRequirementResponse> missingRoles;
    
    @JsonProperty("requirementsRoles")
    List<RoleRequirementResponse> requirementsRoles;
    
    @JsonProperty("assignments")
    List<StaffAssignmentResponse> assignments;
    
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class RoleRequirementResponse {
        
        @JsonProperty("accountType")
        String accountType;
        
        @JsonProperty("quantity")
        int quantity;
    }
    
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class StaffAssignmentResponse {
        
        @JsonProperty("profileId")
        String profileId;
        
        @JsonProperty("fullName")
        String fullName;
        
        @JsonProperty("accountType")
        String accountType;
        
        @JsonProperty("status")
        String status;
    }
}
