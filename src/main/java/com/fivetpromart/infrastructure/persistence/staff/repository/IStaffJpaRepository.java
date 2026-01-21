package com.fivetpromart.infrastructure.persistence.staff.repository;

import com.fivetpromart.infrastructure.persistence.staff.StaffDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IStaffJpaRepository extends JpaRepository<StaffDbo, String>, JpaSpecificationExecutor<StaffDbo> {
    
    /**
     * Find staff by ID, only active
     */
    @Query("SELECT s FROM StaffDbo s WHERE s.profileId = :profileId AND s.isActive = true")
    Optional<StaffDbo> findByProfileIdAndIsActiveTrue(@Param("profileId") String profileId);
    
    /**
     * Find all active staff
     */
    @Query("SELECT s FROM StaffDbo s WHERE s.isActive = true")
    List<StaffDbo> findAllActive();
    
    Optional<StaffDbo> findByUsername(String username);
    Optional<StaffDbo> findByEmail(String email);
    Optional<StaffDbo> findByUserId(String userId);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    
    /**
     * Find active staff by username
     */
    @Query("SELECT s FROM StaffDbo s WHERE s.username = :username AND s.isActive = true")
    Optional<StaffDbo> findByUsernameAndIsActiveTrue(@Param("username") String username);
    
    /**
     * Search active staff
     */
    @Query("SELECT s FROM StaffDbo s WHERE (LOWER(s.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(s.username) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND s.isActive = true")
    List<StaffDbo> searchActiveStaff(@Param("keyword") String keyword);
}
