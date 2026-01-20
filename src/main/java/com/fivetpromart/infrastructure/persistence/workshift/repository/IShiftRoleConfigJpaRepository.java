package com.fivetpromart.infrastructure.persistence.workshift.repository;

import com.fivetpromart.infrastructure.persistence.workshift.ShiftRoleConfigDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IShiftRoleConfigJpaRepository extends JpaRepository<ShiftRoleConfigDbo, String> {
    
    List<ShiftRoleConfigDbo> findByIsActive(boolean isActive);
}
