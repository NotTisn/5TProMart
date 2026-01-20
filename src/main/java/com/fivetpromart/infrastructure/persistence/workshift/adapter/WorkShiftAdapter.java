package com.fivetpromart.infrastructure.persistence.workshift.adapter;

import com.fivetpromart.application.port.out.IWorkShiftRepository;
import com.fivetpromart.domain.model.WorkShift;
import com.fivetpromart.infrastructure.persistence.workshift.mapper.WorkShiftPersistenceMapper;
import com.fivetpromart.infrastructure.persistence.workshift.repository.IWorkShiftJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WorkShiftAdapter implements IWorkShiftRepository {
    
    private final IWorkShiftJpaRepository jpaRepository;
    private final WorkShiftPersistenceMapper mapper;
    
    @Override
    public WorkShift save(WorkShift workShift) {
        var dbo = mapper.toDbo(workShift);
        var saved = jpaRepository.save(dbo);
        return mapper.toDomain(saved);
    }
    
    @Override
    public Optional<WorkShift> findById(String id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    public List<WorkShift> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<WorkShift> findByIsActive(boolean isActive) {
        return jpaRepository.findByIsActive(isActive).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean existsById(String id) {
        return jpaRepository.existsById(id);
    }
}
