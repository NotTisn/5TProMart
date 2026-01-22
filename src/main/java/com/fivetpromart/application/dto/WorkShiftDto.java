package com.fivetpromart.application.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkShiftDto {
    
    String id;
    String shiftName;
    LocalTime startTime;
    LocalTime endTime;
    boolean isActive;
    
    // Role config info
    String roleConfigId;
    String roleConfigName;
}
