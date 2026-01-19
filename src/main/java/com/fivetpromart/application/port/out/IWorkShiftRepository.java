package com.fivetpromart.application.port.out;

import com.fivetpromart.domain.model.WorkShift;

import java.util.List;
import java.util.Optional;

public interface IWorkShiftRepository {
    
    WorkShift save(WorkShift workShift);
    
    Optional<WorkShift> findById(String id);
    
    List<WorkShift> findAll();
    
    List<WorkShift> findByIsActive(boolean isActive);
    
    boolean existsById(String id);
}
