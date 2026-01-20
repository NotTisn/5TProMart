package com.fivetpromart.application.port.out;

import com.fivetpromart.domain.model.ShiftRoleConfig;

import java.util.List;
import java.util.Optional;

public interface IShiftRoleConfigRepository {
    
    ShiftRoleConfig save(ShiftRoleConfig config);
    
    Optional<ShiftRoleConfig> findById(String id);
    
    List<ShiftRoleConfig> findAll();
    
    List<ShiftRoleConfig> findByIsActive(boolean isActive);
    
    boolean existsById(String id);
}
