package com.fivetpromart.domain.model.salary;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SalaryRoleConfig {
    
    String id;
    String role;
    BigDecimal hourlyRate;
    Instant updatedAt;
    
    // Factory method for creating new config
    public static SalaryRoleConfig create(
            String id,
            String role,
            BigDecimal hourlyRate
    ) {
        if (hourlyRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Hourly rate cannot be negative.");
        }
        
        return SalaryRoleConfig.builder()
                .id(id)
                .role(role)
                .hourlyRate(hourlyRate)
                .updatedAt(Instant.now())
                .build();
    }
    
    // Factory method for reconstitution
    public static SalaryRoleConfig reconstitute(
            String id,
            String role,
            BigDecimal hourlyRate,
            Instant updatedAt
    ) {
        return SalaryRoleConfig.builder()
                .id(id)
                .role(role)
                .hourlyRate(hourlyRate)
                .updatedAt(updatedAt)
                .build();
    }
    
    // Business logic: Update hourly rate
    public void updateHourlyRate(BigDecimal newRate) {
        if (newRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Hourly rate cannot be negative.");
        }
        this.hourlyRate = newRate;
        this.updatedAt = Instant.now();
    }
}
