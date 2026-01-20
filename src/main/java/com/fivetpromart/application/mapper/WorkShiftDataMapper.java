package com.fivetpromart.application.mapper;

import com.fivetpromart.application.dto.WorkShiftDto;
import com.fivetpromart.domain.model.WorkShift;
import org.springframework.stereotype.Component;

@Component
public class WorkShiftDataMapper {
    
    public WorkShiftDto toDto(WorkShift domain) {
        if (domain == null) return null;
        
        return WorkShiftDto.builder()
                .id(domain.getId())
                .shiftName(domain.getShiftName())
                .startTime(domain.getStartTime())
                .endTime(domain.getEndTime())
                .isActive(domain.isActive())
                .roleConfigId(domain.getRoleConfigId())
                .roleConfigName(domain.getRoleConfigName())
                .build();
    }
}
