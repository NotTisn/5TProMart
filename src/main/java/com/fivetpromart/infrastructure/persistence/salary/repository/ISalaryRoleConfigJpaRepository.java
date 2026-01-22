package com.fivetpromart.infrastructure.persistence.salary.repository;

import com.fivetpromart.infrastructure.persistence.salary.entity.SalaryRoleConfigDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISalaryRoleConfigJpaRepository extends JpaRepository<SalaryRoleConfigDbo, String> {
    
    Optional<SalaryRoleConfigDbo> findByRole(String role);
}
