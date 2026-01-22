package com.fivetpromart.infrastructure.persistence.salary.repository;

import com.fivetpromart.infrastructure.persistence.salary.entity.DailySalaryDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IDailySalaryJpaRepository extends JpaRepository<DailySalaryDbo, String> {
    
    @Query("SELECT d FROM DailySalaryDbo d WHERE d.date BETWEEN :startDate AND :endDate ORDER BY d.date")
    List<DailySalaryDbo> findByDateRange(@Param("startDate") LocalDate startDate, 
                                          @Param("endDate") LocalDate endDate);
    
    @Query("SELECT d FROM DailySalaryDbo d WHERE d.userId = :userId AND d.date BETWEEN :startDate AND :endDate ORDER BY d.date")
    List<DailySalaryDbo> findByUserIdAndDateRange(@Param("userId") String userId,
                                                    @Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate);
    
    boolean existsByDate(LocalDate date);
    
    boolean existsByUserIdAndDate(String userId, LocalDate date);
}
