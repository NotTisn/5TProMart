package com.fivetpromart.presentation.mapper;

import com.fivetpromart.application.dto.WorkShiftDto;
import com.fivetpromart.application.dto.command.CreateWorkShiftCommand;
import com.fivetpromart.application.dto.command.UpdateWorkShiftCommand;
import com.fivetpromart.presentation.dto.request.CreateWorkShiftRequest;
import com.fivetpromart.presentation.dto.request.UpdateWorkShiftRequest;
import com.fivetpromart.presentation.dto.response.CreateWorkShiftResponse;
import com.fivetpromart.presentation.dto.response.WorkShiftResponse;
import org.springframework.stereotype.Component;

@Component
public class WorkShiftPresentationMapper {
    
    public CreateWorkShiftCommand toCommand(CreateWorkShiftRequest request) {
        return CreateWorkShiftCommand.builder()
                .shiftName(request.getShiftName())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .roleConfigId(request.getRoleConfigId())
                .build();
    }
    
    public WorkShiftResponse toResponse(WorkShiftDto dto) {
        return WorkShiftResponse.builder()
                .id(dto.getId())
                .shiftName(dto.getShiftName())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .isActive(dto.isActive())
                .roleConfig(WorkShiftResponse.RoleConfigInfo.builder()
                        .id(dto.getRoleConfigId())
                        .configName(dto.getRoleConfigName())
                        .build())
                .build();
    }
    
    public CreateWorkShiftResponse toCreateResponse(WorkShiftDto dto) {
        return CreateWorkShiftResponse.builder()
                .id(dto.getId())
                .shiftName(dto.getShiftName())
                .isActive(dto.isActive())
                .build();
    }
    
    public UpdateWorkShiftCommand toUpdateCommand(UpdateWorkShiftRequest request, String id) {
        return UpdateWorkShiftCommand.builder()
                .id(id)
                .shiftName(request.getShiftName())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .roleConfigId(request.getRoleConfigId())
                .build();
    }
}
