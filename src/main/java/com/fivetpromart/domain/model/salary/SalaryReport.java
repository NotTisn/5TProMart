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
 * Aggregate data for salary report
 */
@Getter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SalaryReport {
    
    LocalDate startDate;
    LocalDate endDate;
    BigDecimal totalSalaryCost;
    Double totalWorkHours;
    Integer totalStaffs;
    List<StaffSalaryDetail> staffDetails;
    
    @Getter
    @Builder
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class StaffSalaryDetail {
        String userId;
        String fullName;
        String role;
        Double totalWorkHours;
        BigDecimal totalSalary;
    }
}
