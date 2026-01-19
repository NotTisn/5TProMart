package com.fivetpromart.infrastructure.persistence.workshift.repository;

import com.fivetpromart.infrastructure.persistence.workshift.WorkShiftDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IWorkShiftJpaRepository extends JpaRepository<WorkShiftDbo, String> {
    
    List<WorkShiftDbo> findByIsActive(boolean isActive);
}
