package com.fivetpromart.infrastructure.persistence.workshift.adapter;

import com.fivetpromart.application.port.out.IWorkScheduleRepository;
import com.fivetpromart.domain.model.WorkSchedule;
import com.fivetpromart.infrastructure.persistence.workshift.mapper.WorkSchedulePersistenceMapper;
import com.fivetpromart.infrastructure.persistence.workshift.repository.IWorkScheduleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WorkScheduleAdapter implements IWorkScheduleRepository {
    
    private final IWorkScheduleJpaRepository jpaRepository;
    private final WorkSchedulePersistenceMapper mapper;
    
    @Override
    public WorkSchedule save(WorkSchedule schedule) {
        var dbo = mapper.toDbo(schedule);
        var saved = jpaRepository.save(dbo);
        return mapper.toDomain(saved);
    }
    
    @Override
    public void saveAll(List<WorkSchedule> schedules) {
        var dbos = schedules.stream()
                .map(mapper::toDbo)
                .collect(Collectors.toList());
        jpaRepository.saveAll(dbos);
    }
    
    @Override
    public Optional<WorkSchedule> findById(String id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    public Optional<WorkSchedule> findByWorkDateAndWorkShiftId(LocalDate workDate, String workShiftId) {
        return jpaRepository.findByWorkDateAndWorkShiftId(workDate, workShiftId)
                .map(mapper::toDomain);
    }
    
    @Override
    public List<WorkSchedule> findByWorkDate(LocalDate workDate) {
        return jpaRepository.findByWorkDate(workDate).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<WorkSchedule> findByWorkDateBetween(LocalDate startDate, LocalDate endDate) {
        return jpaRepository.findByWorkDateBetween(startDate, endDate).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<WorkSchedule> findByWorkDateBetweenAndWorkShiftId(LocalDate startDate, LocalDate endDate, String workShiftId) {
        return jpaRepository.findByWorkDateBetweenAndWorkShiftId(startDate, endDate, workShiftId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<WorkSchedule> findByWorkDateBetweenAndProfileId(LocalDate startDate, LocalDate endDate, String profileId) {
        return jpaRepository.findByWorkDateBetweenAndProfileId(startDate, endDate, profileId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<WorkSchedule> findByWorkDateAndProfileId(LocalDate workDate, String profileId) {
        return jpaRepository.findByWorkDateAndProfileId(workDate, profileId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean existsByWorkDateAndWorkShiftId(LocalDate workDate, String workShiftId) {
        return jpaRepository.existsByWorkDateAndWorkShiftId(workDate, workShiftId);
    }
}
