package com.fivetpromart.application.mapper;

import com.fivetpromart.application.dto.WorkScheduleDto;
import com.fivetpromart.domain.model.WorkSchedule;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class WorkScheduleDataMapper {
    
    public WorkScheduleDto toDto(WorkSchedule domain) {
        if (domain == null) return null;
        
        return WorkScheduleDto.builder()
                .id(domain.getId())
                .workDate(domain.getWorkDate())
                .workShiftId(domain.getWorkShiftId())
                .shiftName(domain.getShiftName())
                .startTime(domain.getStartTime())
                .endTime(domain.getEndTime())
                .isCompliant(domain.isCompliant())
                .requirements(domain.getRequirements().stream()
                        .map(this::toRequirementDto)
                        .collect(Collectors.toList()))
                .missingRoles(domain.getMissingRoles().stream()
                        .map(this::toRequirementDto)
                        .collect(Collectors.toList()))
                .assignments(domain.getAssignments().stream()
                        .map(this::toAssignmentDto)
                        .collect(Collectors.toList()))
                .build();
    }
    
    private WorkScheduleDto.RoleRequirementDto toRequirementDto(WorkSchedule.RoleRequirement requirement) {
        return WorkScheduleDto.RoleRequirementDto.builder()
                .accountType(requirement.getAccountType())
                .quantity(requirement.getQuantity())
                .build();
    }
    
    private WorkScheduleDto.StaffAssignmentDto toAssignmentDto(WorkSchedule.StaffAssignment assignment) {
        return WorkScheduleDto.StaffAssignmentDto.builder()
                .profileId(assignment.getProfileId())
                .fullName(assignment.getFullName())
                .accountType(assignment.getAccountType())
                .email(assignment.getEmail())
                .phoneNumber(assignment.getPhoneNumber())
                .status(assignment.getStatus())
                .build();
    }
}
