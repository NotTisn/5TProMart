package com.fivetpromart.infrastructure.persistence.staff.repository;

import com.fivetpromart.infrastructure.persistence.staff.StaffDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IStaffJpaRepository extends JpaRepository<StaffDbo, String>, JpaSpecificationExecutor<StaffDbo> {
    Optional<StaffDbo> findByUsername(String username);
    Optional<StaffDbo> findByEmail(String email);
    Optional<StaffDbo> findByUserId(String userId);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
