package com.fivetpromart.application.usecase;

import com.fivetpromart.application.port.out.IDailySalaryRepository;
import com.fivetpromart.application.port.out.ISalaryRoleConfigRepository;
import com.fivetpromart.application.port.out.IStaffRepository;
import com.fivetpromart.application.port.out.IWorkScheduleRepository;
import com.fivetpromart.domain.model.Staff;
import com.fivetpromart.domain.model.WorkSchedule;
import com.fivetpromart.domain.model.salary.DailySalary;
import com.fivetpromart.domain.model.salary.SalaryReport;
import com.fivetpromart.domain.model.salary.SalaryRoleConfig;
import com.fivetpromart.domain.model.salary.StaffSalaryDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalaryUseCase {

    private final ISalaryRoleConfigRepository salaryRoleConfigRepository;
    private final IDailySalaryRepository dailySalaryRepository;
    private final IWorkScheduleRepository workScheduleRepository;
    private final IStaffRepository staffRepository;

    /**
     * Get all salary configurations
     */
    @Transactional(readOnly = true)
    public List<SalaryRoleConfig> getAllSalaryConfigs() {
        log.info("Getting all salary configurations");
        return salaryRoleConfigRepository.findAll();
    }

    /**
     * Update salary configurations
     */
    @Transactional
    public List<SalaryRoleConfig> updateSalaryConfigs(Map<String, BigDecimal> roleRates) {
        log.info("Updating salary configurations for {} roles", roleRates.size());
        
        List<SalaryRoleConfig> updatedConfigs = new ArrayList<>();
        
        for (Map.Entry<String, BigDecimal> entry : roleRates.entrySet()) {
            String role = entry.getKey();
            BigDecimal hourlyRate = entry.getValue();
            
            // Validate hourly rate
            if (hourlyRate.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Hourly rate cannot be negative.");
            }
            
            // Find existing or create new
            Optional<SalaryRoleConfig> existingOpt = salaryRoleConfigRepository.findByRole(role);
            
            SalaryRoleConfig config;
            if (existingOpt.isPresent()) {
                config = existingOpt.get();
                config.updateHourlyRate(hourlyRate);
                log.info("Updating existing config for role: {}", role);
            } else {
                config = SalaryRoleConfig.create(
                        UUID.randomUUID().toString(),
                        role,
                        hourlyRate
                );
                log.info("Creating new config for role: {}", role);
            }
            
            updatedConfigs.add(salaryRoleConfigRepository.save(config));
        }
        
        return updatedConfigs;
    }

    /**
     * Calculate daily salary for a specific date
     */
    @Transactional
    public void calculateDailySalary(LocalDate date) {
        log.info("Calculating daily salary for date: {}", date);
        
        // Validation: date must be before today
        if (!date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Date must be before today.");
        }
        
        // 1. Query WorkSchedule for the date
        List<WorkSchedule> schedules = workScheduleRepository.findByWorkDate(date);
        
        if (schedules.isEmpty()) {
            log.warn("No work schedules found for date: {}", date);
            return;
        }
        
        // 2. Query all salary role configs
        List<SalaryRoleConfig> configs = salaryRoleConfigRepository.findAll();
        Map<String, BigDecimal> roleRateMap = configs.stream()
                .collect(Collectors.toMap(
                        SalaryRoleConfig::getRole,
                        SalaryRoleConfig::getHourlyRate
                ));
        
        if (roleRateMap.isEmpty()) {
            throw new IllegalStateException("No salary configurations found. Please configure salary rates first.");
        }
        
        // 3. Process each schedule and calculate salaries
        List<DailySalary> dailySalaries = new ArrayList<>();
        
        for (WorkSchedule schedule : schedules) {
            // Calculate work hours for this shift
            double workHours = calculateWorkHours(schedule.getStartTime(), schedule.getEndTime());
            
            // Process each staff assignment
            for (WorkSchedule.StaffAssignment assignment : schedule.getAssignments()) {
                String userId = assignment.getProfileId();
                String role = assignment.getAccountType();
                
                // Skip if already calculated
                if (dailySalaryRepository.existsByUserIdAndDate(userId, date)) {
                    log.debug("Daily salary already exists for user {} on date {}", userId, date);
                    continue;
                }
                
                // Get hourly rate for this role
                BigDecimal hourlyRate = roleRateMap.get(role);
                if (hourlyRate == null) {
                    log.warn("No hourly rate configured for role: {}. Skipping user: {}", role, userId);
                    continue;
                }
                
                // Create daily salary record
                DailySalary dailySalary = DailySalary.create(
                        UUID.randomUUID().toString(),
                        userId,
                        date,
                        role,
                        hourlyRate,
                        workHours
                );
                
                dailySalaries.add(dailySalary);
                log.debug("Created daily salary for user {} - role: {}, hours: {}, amount: {}",
                        userId, role, workHours, dailySalary.getDailySalary());
            }
        }
        
        // Save all daily salaries
        if (!dailySalaries.isEmpty()) {
            dailySalaryRepository.saveAll(dailySalaries);
            log.info("Saved {} daily salary records for date {}", dailySalaries.size(), date);
        } else {
            log.info("No new daily salary records to save for date {}", date);
        }
    }

    /**
     * Get salary report for date range
     */
    @Transactional(readOnly = true)
    public SalaryReport getSalaryReport(LocalDate startDate, LocalDate endDate) {
        log.info("Getting salary report from {} to {}", startDate, endDate);
        
        // Validation
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must before end date");
        }
        
        // Get all daily salaries in range
        List<DailySalary> dailySalaries = dailySalaryRepository.findByDateRange(startDate, endDate);
        
        if (dailySalaries.isEmpty()) {
            return SalaryReport.builder()
                    .startDate(startDate)
                    .endDate(endDate)
                    .totalSalaryCost(BigDecimal.ZERO)
                    .totalWorkHours(0.0)
                    .totalStaffs(0)
                    .staffDetails(new ArrayList<>())
                    .build();
        }
        
        // Group by userId and aggregate
        Map<String, List<DailySalary>> groupedByUser = dailySalaries.stream()
                .collect(Collectors.groupingBy(DailySalary::getUserId));
        
        List<SalaryReport.StaffSalaryDetail> staffDetails = new ArrayList<>();
        BigDecimal totalCost = BigDecimal.ZERO;
        double totalHours = 0.0;
        
        for (Map.Entry<String, List<DailySalary>> entry : groupedByUser.entrySet()) {
            String userId = entry.getKey();
            List<DailySalary> userSalaries = entry.getValue();
            
            // Aggregate for this staff
            BigDecimal staffTotalSalary = userSalaries.stream()
                    .map(DailySalary::getDailySalary)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            double staffTotalHours = userSalaries.stream()
                    .mapToDouble(DailySalary::getWorkHours)
                    .sum();
            
            // Get staff info from first record (role might change, take most recent)
            DailySalary latestRecord = userSalaries.stream()
                    .max(Comparator.comparing(DailySalary::getDate))
                    .orElse(userSalaries.get(0));
            
            String role = latestRecord.getRole();
            
            // TODO: Get full name from Staff/Profile repository
            String fullName = "Staff " + userId; // Placeholder
            
            staffDetails.add(SalaryReport.StaffSalaryDetail.builder()
                    .userId(userId)
                    .fullName(fullName)
                    .role(role)
                    .totalWorkHours(staffTotalHours)
                    .totalSalary(staffTotalSalary)
                    .build());
            
            totalCost = totalCost.add(staffTotalSalary);
            totalHours += staffTotalHours;
        }
        
        // Sort by total salary descending
        staffDetails.sort((a, b) -> b.getTotalSalary().compareTo(a.getTotalSalary()));
        
        return SalaryReport.builder()
                .startDate(startDate)
                .endDate(endDate)
                .totalSalaryCost(totalCost)
                .totalWorkHours(totalHours)
                .totalStaffs(groupedByUser.size())
                .staffDetails(staffDetails)
                .build();
    }

    /**
     * Get salary detail for specific staff
     */
    @Transactional(readOnly = true)
    public StaffSalaryDetail getStaffSalaryDetail(String userId, LocalDate startDate, LocalDate endDate) {
        log.info("Getting salary detail for staff {} from {} to {}", userId, startDate, endDate);

        // Validation
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must before end date");
        }

        // Fetch staff info SAFELY
        Staff staff = staffRepository.findByUserId(userId).orElse(null);
        String fullName = (staff != null) ? staff.getFullName() : "Unknown Staff (" + userId + ")";

        // Get daily salaries for this staff
        List<DailySalary> dailySalaries = dailySalaryRepository.findByUserIdAndDateRange(userId, startDate, endDate);

        if (dailySalaries.isEmpty()) {
            return StaffSalaryDetail.builder()
                    .userId(userId)
                    .fullName(fullName) // Safe to use now
                    .role(staff.getAccountType())
                    .startDate(startDate)
                    .endDate(endDate)
                    .totalSalary(BigDecimal.ZERO)
                    .totalWorkHours(0.0)
                    .dailyDetails(new ArrayList<>())
                    .build();
        }

        // Aggregate totals
        BigDecimal totalSalary = dailySalaries.stream()
                .map(DailySalary::getDailySalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        double totalHours = dailySalaries.stream()
                .mapToDouble(DailySalary::getWorkHours)
                .sum();

        // Get role from latest record
        DailySalary latestRecord = dailySalaries.stream()
                .max(Comparator.comparing(DailySalary::getDate))
                .orElse(dailySalaries.get(0));

        String role = latestRecord.getRole();

        // Build daily details
        List<StaffSalaryDetail.DailyDetail> dailyDetails = dailySalaries.stream()
                .sorted(Comparator.comparing(DailySalary::getDate))
                .map(ds -> StaffSalaryDetail.DailyDetail.builder()
                        .date(ds.getDate())
                        .workHours(ds.getWorkHours())
                        .hourlyRate(ds.getHourlyRate())
                        .dailyAmount(ds.getDailySalary())
                        .build())
                .collect(Collectors.toList());

        return StaffSalaryDetail.builder()
                .userId(userId)
                .fullName(fullName) // Safe to use now
                .role(role)
                .startDate(startDate)
                .endDate(endDate)
                .totalSalary(totalSalary)
                .totalWorkHours(totalHours)
                .dailyDetails(dailyDetails)
                .build();
    }

    /**
     * Calculate work hours between start and end time
     */
    private double calculateWorkHours(LocalTime startTime, LocalTime endTime) {
        Duration duration = Duration.between(startTime, endTime);
        return duration.toMinutes() / 60.0;
    }
}
