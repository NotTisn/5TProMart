package com.fivetpromart.application.mapper;

import com.fivetpromart.application.dto.ShiftRoleConfigDto;
import com.fivetpromart.domain.model.ShiftRoleConfig;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ShiftRoleConfigDataMapper {
    
    public ShiftRoleConfigDto toDto(ShiftRoleConfig domain) {
        if (domain == null) return null;
        
        return ShiftRoleConfigDto.builder()
                .id(domain.getId())
                .configName(domain.getConfigName())
                .description(domain.getDescription())
                .isActive(domain.isActive())
                .requirements(domain.getRequirements().stream()
                        .map(this::toRequirementDto)
                        .collect(Collectors.toList()))
                .build();
    }
    
    private ShiftRoleConfigDto.RoleRequirementDto toRequirementDto(ShiftRoleConfig.RoleRequirement requirement) {
        return ShiftRoleConfigDto.RoleRequirementDto.builder()
                .accountType(requirement.getAccountType())
                .quantity(requirement.getQuantity())
                .build();
    }
    
    public ShiftRoleConfig.RoleRequirement toRequirementDomain(ShiftRoleConfigDto.RoleRequirementDto dto) {
        return ShiftRoleConfig.RoleRequirement.of(
                dto.getAccountType(),
                dto.getQuantity()
        );
    }
}
