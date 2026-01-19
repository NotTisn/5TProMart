package com.fivetpromart.application.usecase;

import com.fivetpromart.application.dto.ShiftRoleConfigDto;
import com.fivetpromart.application.dto.command.CreateRoleConfigCommand;
import com.fivetpromart.application.mapper.ShiftRoleConfigDataMapper;
import com.fivetpromart.application.port.out.IShiftRoleConfigRepository;
import com.fivetpromart.domain.model.ShiftRoleConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShiftRoleConfigUseCase {
    
    private final IShiftRoleConfigRepository repository;
    private final ShiftRoleConfigDataMapper mapper;
    
    @Transactional
    public ShiftRoleConfigDto createRoleConfig(CreateRoleConfigCommand command) {
        // Convert requirements
        List<ShiftRoleConfig.RoleRequirement> requirements = command.getRequirements().stream()
                .map(r -> ShiftRoleConfig.RoleRequirement.of(r.getAccountType(), r.getQuantity()))
                .collect(Collectors.toList());
        
        // Create domain object
        ShiftRoleConfig config = ShiftRoleConfig.create(
                UUID.randomUUID().toString(),
                command.getConfigName(),
                command.getDescription(),
                requirements
        );
        
        // Save
        ShiftRoleConfig saved = repository.save(config);
        
        return mapper.toDto(saved);
    }
    
    @Transactional(readOnly = true)
    public List<ShiftRoleConfigDto> getRoleConfigs(Boolean isActive) {
        List<ShiftRoleConfig> configs;
        
        if (isActive != null) {
            configs = repository.findByIsActive(isActive);
        } else {
            configs = repository.findAll();
        }
        
        return configs.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public ShiftRoleConfigDto getRoleConfigById(String id) {
        ShiftRoleConfig config = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role config not found with id: " + id));
        
        return mapper.toDto(config);
    }
}
