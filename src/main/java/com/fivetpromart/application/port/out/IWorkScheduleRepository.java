package com.fivetpromart.application.port.out;

import com.fivetpromart.domain.model.WorkSchedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IWorkScheduleRepository {
    
    WorkSchedule save(WorkSchedule schedule);
    
    void saveAll(List<WorkSchedule> schedules);
    
    Optional<WorkSchedule> findById(String id);
    
    Optional<WorkSchedule> findByWorkDateAndWorkShiftId(LocalDate workDate, String workShiftId);
    
    List<WorkSchedule> findByWorkDate(LocalDate workDate);
    
    List<WorkSchedule> findByWorkDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<WorkSchedule> findByWorkDateBetweenAndWorkShiftId(LocalDate startDate, LocalDate endDate, String workShiftId);
    
    List<WorkSchedule> findByWorkDateBetweenAndProfileId(LocalDate startDate, LocalDate endDate, String profileId);
    
    List<WorkSchedule> findByWorkDateAndProfileId(LocalDate workDate, String profileId);
    
    boolean existsByWorkDateAndWorkShiftId(LocalDate workDate, String workShiftId);
}
