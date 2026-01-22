package com.fivetpromart.infrastructure.persistence.workshift.mapper;

import com.fivetpromart.domain.model.WorkSchedule;
import com.fivetpromart.infrastructure.persistence.workshift.RoleRequirementDbo;
import com.fivetpromart.infrastructure.persistence.workshift.StaffAssignmentDbo;
import com.fivetpromart.infrastructure.persistence.workshift.WorkScheduleDbo;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class WorkSchedulePersistenceMapper {
    
    public WorkScheduleDbo toDbo(WorkSchedule domain) {
        if (domain == null) return null;
        
        return WorkScheduleDbo.builder()
                .id(domain.getId())
                .workDate(domain.getWorkDate())
                .workShiftId(domain.getWorkShiftId())
                .shiftName(domain.getShiftName())
                .startTime(domain.getStartTime())
                .endTime(domain.getEndTime())
                .isCompliant(domain.isCompliant())
                .requirements(domain.getRequirements().stream()
                        .map(this::toRequirementDbo)
                        .collect(Collectors.toList()))
                .missingRoles(domain.getMissingRoles().stream()
                        .map(this::toRequirementDbo)
                        .collect(Collectors.toList()))
                .assignments(domain.getAssignments().stream()
                        .map(this::toAssignmentDbo)
                        .collect(Collectors.toList()))
                .build();
    }
    
    public WorkSchedule toDomain(WorkScheduleDbo dbo) {
        if (dbo == null) return null;
        
        return WorkSchedule.reconstitute(
                dbo.getId(),
                dbo.getWorkDate(),
                dbo.getWorkShiftId(),
                dbo.getShiftName(),
                dbo.getStartTime(),
                dbo.getEndTime(),
                dbo.isCompliant(),
                dbo.getRequirements().stream()
                        .map(this::toRequirementDomain)
                        .collect(Collectors.toList()),
                dbo.getMissingRoles().stream()
                        .map(this::toRequirementDomain)
                        .collect(Collectors.toList()),
                dbo.getAssignments().stream()
                        .map(this::toAssignmentDomain)
                        .collect(Collectors.toList())
        );
    }
    
    private RoleRequirementDbo toRequirementDbo(WorkSchedule.RoleRequirement domain) {
        return RoleRequirementDbo.builder()
                .accountType(domain.getAccountType())
                .quantity(domain.getQuantity())
                .build();
    }
    
    private WorkSchedule.RoleRequirement toRequirementDomain(RoleRequirementDbo dbo) {
        return WorkSchedule.RoleRequirement.of(
                dbo.getAccountType(),
                dbo.getQuantity()
        );
    }
    
    private StaffAssignmentDbo toAssignmentDbo(WorkSchedule.StaffAssignment domain) {
        return StaffAssignmentDbo.builder()
                .profileId(domain.getProfileId())
                .fullName(domain.getFullName())
                .accountType(domain.getAccountType())
                .email(domain.getEmail())
                .phoneNumber(domain.getPhoneNumber())
                .status(domain.getStatus())
                .build();
    }
    
    private WorkSchedule.StaffAssignment toAssignmentDomain(StaffAssignmentDbo dbo) {
        return WorkSchedule.StaffAssignment.builder()
                .profileId(dbo.getProfileId())
                .fullName(dbo.getFullName())
                .accountType(dbo.getAccountType())
                .email(dbo.getEmail())
                .phoneNumber(dbo.getPhoneNumber())
                .status(dbo.getStatus())
                .build();
    }
}
