package com.fivetpromart.domain.model.salary;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DailySalary {
    
    String id;
    String userId;
    LocalDate date;
    
    String role;
    BigDecimal hourlyRate;
    
    Double workHours;
    BigDecimal dailySalary;
    
    Instant createdAt;
    
    // Factory method for creating new daily salary
    public static DailySalary create(
            String id,
            String userId,
            LocalDate date,
            String role,
            BigDecimal hourlyRate,
            Double workHours
    ) {
        BigDecimal dailySalary = hourlyRate.multiply(BigDecimal.valueOf(workHours));
        
        return DailySalary.builder()
                .id(id)
                .userId(userId)
                .date(date)
                .role(role)
                .hourlyRate(hourlyRate)
                .workHours(workHours)
                .dailySalary(dailySalary)
                .createdAt(Instant.now())
                .build();
    }
    
    // Factory method for reconstitution
    public static DailySalary reconstitute(
            String id,
            String userId,
            LocalDate date,
            String role,
            BigDecimal hourlyRate,
            Double workHours,
            BigDecimal dailySalary,
            Instant createdAt
    ) {
        return DailySalary.builder()
                .id(id)
                .userId(userId)
                .date(date)
                .role(role)
                .hourlyRate(hourlyRate)
                .workHours(workHours)
                .dailySalary(dailySalary)
                .createdAt(createdAt)
                .build();
    }
}
