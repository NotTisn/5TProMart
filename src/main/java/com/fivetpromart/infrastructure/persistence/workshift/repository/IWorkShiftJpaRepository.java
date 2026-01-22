package com.fivetpromart.infrastructure.persistence.workshift.repository;

import com.fivetpromart.infrastructure.persistence.workshift.WorkShiftDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IWorkShiftJpaRepository extends JpaRepository<WorkShiftDbo, String> {
    
    /**
     * Find shift by ID, only active
     */
    @Query("SELECT w FROM WorkShiftDbo w WHERE w.id = :id AND w.isActive = true")
    Optional<WorkShiftDbo> findByIdAndIsActiveTrue(@Param("id") String id);
    
    /**
     * Find all active shifts
     */
    List<WorkShiftDbo> findByIsActive(boolean isActive);
    
    /**
     * Search active shifts by name
     */
    @Query("SELECT w FROM WorkShiftDbo w WHERE LOWER(w.shiftName) LIKE LOWER(CONCAT('%', :keyword, '%')) AND w.isActive = true")
    List<WorkShiftDbo> searchActiveShifts(@Param("keyword") String keyword);
}
