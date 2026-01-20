package com.fivetpromart.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkShiftResponse {
    
    @JsonProperty("id")
    String id;
    
    @JsonProperty("shiftName")
    String shiftName;
    
    @JsonFormat(pattern = "HH:mm")
    @JsonProperty("startTime")
    LocalTime startTime;
    
    @JsonFormat(pattern = "HH:mm")
    @JsonProperty("endTime")
    LocalTime endTime;
    
    @JsonProperty("isActive")
    boolean isActive;
    
    @JsonProperty("roleConfig")
    RoleConfigInfo roleConfig;
    
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class RoleConfigInfo {
        
        @JsonProperty("id")
        String id;
        
        @JsonProperty("configName")
        String configName;
    }
}
