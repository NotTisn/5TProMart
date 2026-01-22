package com.fivetpromart.application.port.out;

import com.fivetpromart.domain.model.salary.DailySalary;

import java.time.LocalDate;
import java.util.List;

public interface IDailySalaryRepository {
    
    /**
     * Save daily salary record
     */
    DailySalary save(DailySalary dailySalary);
    
    /**
     * Save multiple daily salary records
     */
    List<DailySalary> saveAll(List<DailySalary> dailySalaries);
    
    /**
     * Find daily salaries by date range
     */
    List<DailySalary> findByDateRange(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find daily salaries by user and date range
     */
    List<DailySalary> findByUserIdAndDateRange(String userId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Check if daily salary exists for date
     */
    boolean existsByDate(LocalDate date);
    
    /**
     * Check if daily salary exists for user and date
     */
    boolean existsByUserIdAndDate(String userId, LocalDate date);
}
