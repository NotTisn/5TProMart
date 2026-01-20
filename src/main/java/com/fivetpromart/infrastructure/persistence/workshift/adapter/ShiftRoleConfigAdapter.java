package com.fivetpromart.infrastructure.persistence.workshift.adapter;

import com.fivetpromart.application.port.out.IShiftRoleConfigRepository;
import com.fivetpromart.domain.model.ShiftRoleConfig;
import com.fivetpromart.infrastructure.persistence.workshift.mapper.ShiftRoleConfigPersistenceMapper;
import com.fivetpromart.infrastructure.persistence.workshift.repository.IShiftRoleConfigJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ShiftRoleConfigAdapter implements IShiftRoleConfigRepository {
    
    private final IShiftRoleConfigJpaRepository jpaRepository;
    private final ShiftRoleConfigPersistenceMapper mapper;
    
    @Override
    public ShiftRoleConfig save(ShiftRoleConfig config) {
        var dbo = mapper.toDbo(config);
        var saved = jpaRepository.save(dbo);
        return mapper.toDomain(saved);
    }
    
    @Override
    public Optional<ShiftRoleConfig> findById(String id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    public List<ShiftRoleConfig> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ShiftRoleConfig> findByIsActive(boolean isActive) {
        return jpaRepository.findByIsActive(isActive).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean existsById(String id) {
        return jpaRepository.existsById(id);
    }
}
