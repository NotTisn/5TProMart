package com.fivetpromart.application.usecase;

import com.fivetpromart.application.dto.WorkScheduleDto;
import com.fivetpromart.application.dto.command.AssignStaffCommand;
import com.fivetpromart.application.dto.command.RemoveStaffCommand;
import com.fivetpromart.application.dto.query.WorkScheduleSearchQuery;
import com.fivetpromart.application.mapper.WorkScheduleDataMapper;
import com.fivetpromart.application.port.out.IShiftRoleConfigRepository;
import com.fivetpromart.application.port.out.IStaffRepository;
import com.fivetpromart.application.port.out.IWorkScheduleRepository;
import com.fivetpromart.application.port.out.IWorkShiftRepository;
import com.fivetpromart.domain.model.ShiftRoleConfig;
import com.fivetpromart.domain.model.Staff;
import com.fivetpromart.domain.model.WorkSchedule;
import com.fivetpromart.domain.model.WorkShift;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkScheduleUseCase {
    
    private final IWorkScheduleRepository scheduleRepository;
    private final IWorkShiftRepository shiftRepository;
    private final IShiftRoleConfigRepository roleConfigRepository;
    private final IStaffRepository staffRepository;
    private final WorkScheduleDataMapper mapper;
    
    @Transactional(readOnly = true)
    public List<WorkScheduleDto> getSchedules(WorkScheduleSearchQuery query) {
        List<WorkSchedule> schedules;
        
        // Apply filters based on query
        if (query.getProfileId() != null) {
            schedules = scheduleRepository.findByWorkDateBetweenAndProfileId(
                    query.getStartDate(),
                    query.getEndDate(),
                    query.getProfileId()
            );
        } else if (query.getWorkShiftId() != null) {
            schedules = scheduleRepository.findByWorkDateBetweenAndWorkShiftId(
                    query.getStartDate(),
                    query.getEndDate(),
                    query.getWorkShiftId()
            );
        } else {
            schedules = scheduleRepository.findByWorkDateBetween(
                    query.getStartDate(),
                    query.getEndDate()
            );
        }
        
        return schedules.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public Map<String, Object> assignStaffToShift(AssignStaffCommand command) {
        // Validate inputs
        WorkShift workShift = shiftRepository.findById(command.getWorkShiftId())
                .orElseThrow(() -> new IllegalArgumentException("Work shift not found"));
        
        ShiftRoleConfig roleConfig = roleConfigRepository.findById(workShift.getRoleConfigId())
                .orElseThrow(() -> new IllegalArgumentException("Role config not found"));
        
        // Fetch all staff info
        List<Staff> staffList = new ArrayList<>();
        for (String staffId : command.getAssignedStaffIds()) {
            Staff staff = staffRepository.findById(staffId)
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found with id: " + staffId));
            staffList.add(staff);
        }
        
        // Validate for all dates first (fail fast approach)
        Map<String, Map<String, String>> allErrors = new HashMap<>();
        
        for (LocalDate workDate : command.getWorkDates()) {
            Map<String, String> dateErrors = validateAssignments(workDate, workShift, staffList);
            if (!dateErrors.isEmpty()) {
                allErrors.put(workDate.toString(), dateErrors);
            }
        }
        
        // If any errors, throw exception
        if (!allErrors.isEmpty()) {
            throw new AssignmentConflictException("Assignment failed due to conflicts", allErrors);
        }
        
        // No errors - proceed with assignments
        List<Map<String, Object>> scheduleStatuses = new ArrayList<>();
        
        for (LocalDate workDate : command.getWorkDates()) {
            WorkSchedule schedule = getOrCreateSchedule(workDate, workShift, roleConfig);
            
            // Create staff assignments
            List<WorkSchedule.StaffAssignment> assignments = staffList.stream()
                    .map(staff -> WorkSchedule.StaffAssignment.create(
                            staff.getUserId(),
                            staff.getFullName(),
                            staff.getAccountType(),
                            staff.getEmail(),
                            staff.getPhoneNumber()
                    ))
                    .collect(Collectors.toList());
            
            // Add assignments to schedule
            schedule.addStaffAssignments(assignments);
            
            // Save
            WorkSchedule saved = scheduleRepository.save(schedule);
            
            // Build status for this date
            Map<String, Object> dateStatus = new HashMap<>();
            dateStatus.put("workDate", workDate.toString());
            dateStatus.put("isCompliant", saved.isCompliant());
            dateStatus.put("missingRoles", saved.getMissingRoles().stream()
                    .map(r -> Map.of(
                            "accountType", r.getAccountType(),
                            "quantity", r.getQuantity()
                    ))
                    .collect(Collectors.toList()));
            
            scheduleStatuses.add(dateStatus);
        }
        
        // Build response
        Map<String, Object> response = new HashMap<>();
        response.put("assignedCount", command.getWorkDates().size());
        response.put("scheduleStatus", scheduleStatuses);
        
        return response;
    }
    
    @Transactional
    public Map<String, Object> removeStaffFromShift(RemoveStaffCommand command) {
        List<Map<String, Object>> results = new ArrayList<>();
        
        for (LocalDate workDate : command.getWorkDates()) {
            for (String workShiftId : command.getWorkShiftIds()) {
                Optional<WorkSchedule> scheduleOpt = scheduleRepository.findByWorkDateAndWorkShiftId(workDate, workShiftId);
                
                if (scheduleOpt.isPresent()) {
                    WorkSchedule schedule = scheduleOpt.get();
                    
                    // Remove staff
                    schedule.removeStaffAssignments(command.getAssignedStaffIds());
                    
                    // Save
                    WorkSchedule saved = scheduleRepository.save(schedule);
                    
                    // Build result
                    Map<String, Object> result = new HashMap<>();
                    result.put("workDate", workDate.toString());
                    result.put("isCompliant", saved.isCompliant());
                    result.put("missingRoles", saved.getMissingRoles().stream()
                            .map(r -> Map.of(
                                    "accountType", r.getAccountType(),
                                    "quantity", r.getQuantity()
                            ))
                            .collect(Collectors.toList()));
                    
                    results.add(result);
                }
            }
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("results", results);
        return response;
    }
    
    // Helper: Get or create schedule for a specific date
    private WorkSchedule getOrCreateSchedule(LocalDate workDate, WorkShift workShift, ShiftRoleConfig roleConfig) {
        Optional<WorkSchedule> existing = scheduleRepository.findByWorkDateAndWorkShiftId(workDate, workShift.getId());
        
        if (existing.isPresent()) {
            return existing.get();
        }
        
        // Create new schedule
        return WorkSchedule.create(
                UUID.randomUUID().toString(),
                workDate,
                workShift,
                roleConfig.getRequirements()
        );
    }
    
    // Helper: Validate staff assignments for a date
    private Map<String, String> validateAssignments(LocalDate workDate, WorkShift newShift, List<Staff> staffList) {
        Map<String, String> errors = new HashMap<>();
        
        for (Staff staff : staffList) {
            // Check if already assigned to this shift on this date
            Optional<WorkSchedule> existingSchedule = scheduleRepository.findByWorkDateAndWorkShiftId(workDate, newShift.getId());
            
            if (existingSchedule.isPresent() && existingSchedule.get().isStaffAssigned(staff.getUserId())) {
                errors.put(staff.getUserId(), String.format("Staff '%s' is already assigned to this shift.", staff.getFullName()));
                continue;
            }
            
            // Check for time conflicts with other shifts on the same date
            List<WorkSchedule> staffSchedulesOnDate = scheduleRepository.findByWorkDateAndProfileId(workDate, staff.getUserId());
            
            for (WorkSchedule schedule : staffSchedulesOnDate) {
                // Check if times overlap
                boolean overlap = newShift.getStartTime().isBefore(schedule.getEndTime()) 
                        && newShift.getEndTime().isAfter(schedule.getStartTime());
                
                if (overlap) {
                    errors.put(staff.getUserId(), String.format("Staff '%s' is already assigned to this shift.", staff.getFullName()));
                    break;
                }
            }
            
            // Check total working hours for the day (max 8 hours)
            long totalHours = newShift.getDurationInHours();
            for (WorkSchedule schedule : staffSchedulesOnDate) {
                totalHours += schedule.getDurationInHours();
            }
            
            if (totalHours > 8) {
                errors.put(staff.getUserId(), String.format("Staff '%s' exceeds 8 working hours that day.", staff.getFullName()));
            }
        }
        
        return errors;
    }
    
    // Custom exception for assignment conflicts
    public static class AssignmentConflictException extends RuntimeException {
        private final Map<String, Map<String, String>> errors;
        
        public AssignmentConflictException(String message, Map<String, Map<String, String>> errors) {
            super(message);
            this.errors = errors;
        }
        
        public Map<String, Map<String, String>> getErrors() {
            return errors;
        }
    }
}
