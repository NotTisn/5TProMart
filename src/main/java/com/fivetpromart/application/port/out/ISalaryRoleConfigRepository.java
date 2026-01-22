package com.fivetpromart.application.port.out;

import com.fivetpromart.domain.model.salary.SalaryRoleConfig;

import java.util.List;
import java.util.Optional;

public interface ISalaryRoleConfigRepository {
    
    /**
     * Find all salary role configurations
     */
    List<SalaryRoleConfig> findAll();
    
    /**
     * Find salary config by role
     */
    Optional<SalaryRoleConfig> findByRole(String role);
    
    /**
     * Save salary role config (create or update)
     */
    SalaryRoleConfig save(SalaryRoleConfig config);
    
    /**
     * Save multiple configs
     */
    List<SalaryRoleConfig> saveAll(List<SalaryRoleConfig> configs);
}
