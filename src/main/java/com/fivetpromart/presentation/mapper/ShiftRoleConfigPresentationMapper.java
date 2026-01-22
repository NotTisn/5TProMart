package com.fivetpromart.presentation.mapper;

import com.fivetpromart.application.dto.ShiftRoleConfigDto;
import com.fivetpromart.application.dto.command.CreateRoleConfigCommand;
import com.fivetpromart.presentation.dto.request.CreateRoleConfigRequest;
import com.fivetpromart.presentation.dto.response.CreateRoleConfigResponse;
import com.fivetpromart.presentation.dto.response.ShiftRoleConfigResponse;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ShiftRoleConfigPresentationMapper {
    
    public CreateRoleConfigCommand toCommand(CreateRoleConfigRequest request) {
        return CreateRoleConfigCommand.builder()
                .configName(request.getConfigName())
                .description(request.getDescription())
                .requirements(request.getRequirements().stream()
                        .map(r -> CreateRoleConfigCommand.RoleRequirementCommand.builder()
                                .accountType(r.getAccountType())
                                .quantity(r.getQuantity())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
    
    public ShiftRoleConfigResponse toResponse(ShiftRoleConfigDto dto) {
        return ShiftRoleConfigResponse.builder()
                .id(dto.getId())
                .configName(dto.getConfigName())
                .description(dto.getDescription())
                .requirements(dto.getRequirements().stream()
                        .map(r -> ShiftRoleConfigResponse.RoleRequirementResponse.builder()
                                .accountType(r.getAccountType())
                                .quantity(r.getQuantity())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
    
    public CreateRoleConfigResponse toCreateResponse(ShiftRoleConfigDto dto) {
        return CreateRoleConfigResponse.builder()
                .id(dto.getId())
                .configName(dto.getConfigName())
                .isActive(dto.isActive())
                .build();
    }
}
