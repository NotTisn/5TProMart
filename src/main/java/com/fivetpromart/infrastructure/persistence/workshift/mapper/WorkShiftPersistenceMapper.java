package com.fivetpromart.infrastructure.persistence.workshift.mapper;

import com.fivetpromart.domain.model.WorkShift;
import com.fivetpromart.infrastructure.persistence.workshift.WorkShiftDbo;
import org.springframework.stereotype.Component;

@Component
public class WorkShiftPersistenceMapper {
    
    public WorkShiftDbo toDbo(WorkShift domain) {
        if (domain == null) return null;
        
        return WorkShiftDbo.builder()
                .id(domain.getId())
                .shiftName(domain.getShiftName())
                .startTime(domain.getStartTime())
                .endTime(domain.getEndTime())
                .isActive(domain.isActive())
                .roleConfigId(domain.getRoleConfigId())
                .roleConfigName(domain.getRoleConfigName())
                .build();
    }
    
    public WorkShift toDomain(WorkShiftDbo dbo) {
        if (dbo == null) return null;
        
        return WorkShift.reconstitute(
                dbo.getId(),
                dbo.getShiftName(),
                dbo.getStartTime(),
                dbo.getEndTime(),
                dbo.isActive(),
                dbo.getRoleConfigId(),
                dbo.getRoleConfigName()
        );
    }
}
