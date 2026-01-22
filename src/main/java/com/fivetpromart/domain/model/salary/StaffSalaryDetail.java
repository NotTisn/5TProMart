package com.fivetpromart.domain.model.salary;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Aggregate data for individual staff salary detail
 */
@Getter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffSalaryDetail {
    
    String userId;
    String fullName;
    String role;
    LocalDate startDate;
    LocalDate endDate;
    BigDecimal totalSalary;
    Double totalWorkHours;
    List<DailyDetail> dailyDetails;
    
    @Getter
    @Builder
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class DailyDetail {
        LocalDate date;
        Double workHours;
        BigDecimal hourlyRate;
        BigDecimal dailyAmount;
    }
}
