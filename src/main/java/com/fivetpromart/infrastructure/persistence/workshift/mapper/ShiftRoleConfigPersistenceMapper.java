package com.fivetpromart.infrastructure.persistence.workshift.mapper;

import com.fivetpromart.domain.model.ShiftRoleConfig;
import com.fivetpromart.infrastructure.persistence.workshift.RoleRequirementDbo;
import com.fivetpromart.infrastructure.persistence.workshift.ShiftRoleConfigDbo;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ShiftRoleConfigPersistenceMapper {
    
    public ShiftRoleConfigDbo toDbo(ShiftRoleConfig domain) {
        if (domain == null) return null;
        
        return ShiftRoleConfigDbo.builder()
                .id(domain.getId())
                .configName(domain.getConfigName())
                .description(domain.getDescription())
                .isActive(domain.isActive())
                .requirements(domain.getRequirements().stream()
                        .map(this::toRequirementDbo)
                        .collect(Collectors.toList()))
                .build();
    }
    
    public ShiftRoleConfig toDomain(ShiftRoleConfigDbo dbo) {
        if (dbo == null) return null;
        
        return ShiftRoleConfig.reconstitute(
                dbo.getId(),
                dbo.getConfigName(),
                dbo.getDescription(),
                dbo.isActive(),
                dbo.getRequirements().stream()
                        .map(this::toRequirementDomain)
                        .collect(Collectors.toList())
        );
    }
    
    private RoleRequirementDbo toRequirementDbo(ShiftRoleConfig.RoleRequirement domain) {
        return RoleRequirementDbo.builder()
                .accountType(domain.getAccountType())
                .quantity(domain.getQuantity())
                .build();
    }
    
    private ShiftRoleConfig.RoleRequirement toRequirementDomain(RoleRequirementDbo dbo) {
        return ShiftRoleConfig.RoleRequirement.of(
                dbo.getAccountType(),
                dbo.getQuantity()
        );
    }
}
