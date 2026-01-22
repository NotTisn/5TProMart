package com.fivetpromart.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkShift {
    
    String id;
    String shiftName;
    LocalTime startTime;
    LocalTime endTime;
    boolean isActive;
    
    // Role config reference
    String roleConfigId;
    String roleConfigName;
    
    // Factory method for creating new shift
    public static WorkShift create(
            String id,
            String shiftName,
            LocalTime startTime,
            LocalTime endTime,
            String roleConfigId,
            String roleConfigName
    ) {
        validateTimes(startTime, endTime);
        
        return WorkShift.builder()
                .id(id)
                .shiftName(shiftName)
                .startTime(startTime)
                .endTime(endTime)
                .isActive(true)
                .roleConfigId(roleConfigId)
                .roleConfigName(roleConfigName)
                .build();
    }
    
    // Factory method for reconstitution from database
    public static WorkShift reconstitute(
            String id,
            String shiftName,
            LocalTime startTime,
            LocalTime endTime,
            boolean isActive,
            String roleConfigId,
            String roleConfigName
    ) {
        return WorkShift.builder()
                .id(id)
                .shiftName(shiftName)
                .startTime(startTime)
                .endTime(endTime)
                .isActive(isActive)
                .roleConfigId(roleConfigId)
                .roleConfigName(roleConfigName)
                .build();
    }
    
    public void deactivate() {
        this.isActive = false;
    }
    
    public void activate() {
        this.isActive = true;
    }
    
    private static void validateTimes(LocalTime startTime, LocalTime endTime) {
        if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
            throw new IllegalArgumentException("End time must be after start time");
        }
    }
    
    public long getDurationInHours() {
        long hours = java.time.Duration.between(startTime, endTime).toHours();
        return hours;
    }
}
