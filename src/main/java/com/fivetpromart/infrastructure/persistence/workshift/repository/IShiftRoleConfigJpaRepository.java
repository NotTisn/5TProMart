package com.fivetpromart.infrastructure.persistence.workshift.repository;

import com.fivetpromart.infrastructure.persistence.workshift.ShiftRoleConfigDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IShiftRoleConfigJpaRepository extends JpaRepository<ShiftRoleConfigDbo, String> {
    
    /**
     * Find config by ID, only active
     */
    @Query("SELECT c FROM ShiftRoleConfigDbo c WHERE c.id = :id AND c.isActive = true")
    Optional<ShiftRoleConfigDbo> findByIdAndIsActiveTrue(@Param("id") String id);
    
    /**
     * Find all active configs
     */
    List<ShiftRoleConfigDbo> findByIsActive(boolean isActive);
    
    /**
     * Search active configs
     */
    @Query("SELECT c FROM ShiftRoleConfigDbo c WHERE LOWER(c.configName) LIKE LOWER(CONCAT('%', :keyword, '%')) AND c.isActive = true")
    List<ShiftRoleConfigDbo> searchActiveConfigs(@Param("keyword") String keyword);
}
