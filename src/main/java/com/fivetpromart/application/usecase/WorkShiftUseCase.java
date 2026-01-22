package com.fivetpromart.application.usecase;

import com.fivetpromart.application.dto.WorkShiftDto;
import com.fivetpromart.application.dto.command.CreateWorkShiftCommand;
import com.fivetpromart.application.mapper.WorkShiftDataMapper;
import com.fivetpromart.application.port.out.IShiftRoleConfigRepository;
import com.fivetpromart.application.port.out.IWorkShiftRepository;
import com.fivetpromart.domain.model.ShiftRoleConfig;
import com.fivetpromart.domain.model.WorkShift;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkShiftUseCase {
    
    private final IWorkShiftRepository workShiftRepository;
    private final IShiftRoleConfigRepository roleConfigRepository;
    private final WorkShiftDataMapper mapper;
    
    @Transactional
    public WorkShiftDto createWorkShift(CreateWorkShiftCommand command) {
        // Validate role config exists
        ShiftRoleConfig roleConfig = roleConfigRepository.findById(command.getRoleConfigId())
                .orElseThrow(() -> new IllegalArgumentException("Role config not found with id: " + command.getRoleConfigId()));
        
        if (!roleConfig.isActive()) {
            throw new IllegalArgumentException("Role config is not active");
        }
        
        // Create work shift
        WorkShift workShift = WorkShift.create(
                UUID.randomUUID().toString(),
                command.getShiftName(),
                command.getStartTime(),
                command.getEndTime(),
                roleConfig.getId(),
                roleConfig.getConfigName()
        );
        
        // Save
        WorkShift saved = workShiftRepository.save(workShift);
        
        return mapper.toDto(saved);
    }
    
    @Transactional(readOnly = true)
    public List<WorkShiftDto> getWorkShifts(Boolean isActive, Boolean includeDeleted) {
        List<WorkShift> shifts;
        
        if (includeDeleted != null && includeDeleted) {
            // Return all including deleted
            if (isActive != null) {
                shifts = workShiftRepository.findByIsActive(isActive);
            } else {
                shifts = workShiftRepository.findAllIncludingDeleted();
            }
        } else {
            // Default: only active records
            if (isActive != null) {
                shifts = workShiftRepository.findByIsActive(isActive);
            } else {
                shifts = workShiftRepository.findAll();
            }
        }
        
        return shifts.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public WorkShiftDto getWorkShiftById(String id) {
        WorkShift shift = workShiftRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Work shift not found with id: " + id));
        
        return mapper.toDto(shift);
    }

    @Transactional
    public WorkShiftDto restoreWorkShift(String shiftId) {
        WorkShift shift = workShiftRepository.findByIdIncludingDeleted(shiftId)
                .orElseThrow(() -> new IllegalArgumentException("Work shift not found with id: " + shiftId));
        
        if (shift.isActive()) {
            // Already active, no need to restore
            return mapper.toDto(shift);
        }
        
        shift.activate();
        return mapper.toDto(workShiftRepository.save(shift));
    }
}
