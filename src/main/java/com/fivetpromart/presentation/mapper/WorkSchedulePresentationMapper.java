package com.fivetpromart.presentation.mapper;

import com.fivetpromart.application.dto.WorkScheduleDto;
import com.fivetpromart.application.dto.command.AssignStaffCommand;
import com.fivetpromart.application.dto.command.RemoveStaffCommand;
import com.fivetpromart.presentation.dto.request.AssignStaffRequest;
import com.fivetpromart.presentation.dto.request.RemoveStaffRequest;
import com.fivetpromart.presentation.dto.response.WorkScheduleResponse;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class WorkSchedulePresentationMapper {
    
    public AssignStaffCommand toAssignCommand(AssignStaffRequest request) {
        return AssignStaffCommand.builder()
                .workDates(request.getWorkDates())
                .workShiftId(request.getWorkShiftId())
                .assignedStaffIds(request.getAssignedStaffIds())
                .build();
    }
    
    public RemoveStaffCommand toRemoveCommand(RemoveStaffRequest request) {
        return RemoveStaffCommand.builder()
                .workDates(request.getWorkDates())
                .workShiftIds(request.getWorkShiftIds())
                .assignedStaffIds(request.getAssignedStaffIds())
                .build();
    }
    
    public WorkScheduleResponse toResponse(WorkScheduleDto dto) {
        return WorkScheduleResponse.builder()
                .id(dto.getId())
                .workDate(dto.getWorkDate())
                .workShiftId(dto.getWorkShiftId())
                .shiftName(dto.getShiftName())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .isCompliant(dto.isCompliant())
                .missingRoles(dto.getMissingRoles().stream()
                        .map(r -> WorkScheduleResponse.RoleRequirementResponse.builder()
                                .accountType(r.getAccountType())
                                .quantity(r.getQuantity())
                                .build())
                        .collect(Collectors.toList()))
                .requirementsRoles(dto.getRequirements().stream()
                        .map(r -> WorkScheduleResponse.RoleRequirementResponse.builder()
                                .accountType(r.getAccountType())
                                .quantity(r.getQuantity())
                                .build())
                        .collect(Collectors.toList()))
                .assignments(dto.getAssignments().stream()
                        .map(a -> WorkScheduleResponse.StaffAssignmentResponse.builder()
                                .profileId(a.getProfileId())
                                .fullName(a.getFullName())
                                .accountType(a.getAccountType())
                                .status(a.getStatus())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
