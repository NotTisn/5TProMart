package com.fivetpromart.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkSchedule {
    
    String id;
    LocalDate workDate;
    String workShiftId;
    String shiftName;
    LocalTime startTime;
    LocalTime endTime;
    
    boolean isCompliant;
    
    @Builder.Default
    List<RoleRequirement> requirements = new ArrayList<>();
    
    @Builder.Default
    List<RoleRequirement> missingRoles = new ArrayList<>();
    
    @Builder.Default
    List<StaffAssignment> assignments = new ArrayList<>();
    
    // Factory method for creating new schedule
    public static WorkSchedule create(
            String id,
            LocalDate workDate,
            WorkShift workShift,
            List<ShiftRoleConfig.RoleRequirement> configRequirements
    ) {
        List<RoleRequirement> requirements = configRequirements.stream()
                .map(r -> RoleRequirement.of(r.getAccountType(), r.getQuantity()))
                .collect(Collectors.toList());
        
        return WorkSchedule.builder()
                .id(id)
                .workDate(workDate)
                .workShiftId(workShift.getId())
                .shiftName(workShift.getShiftName())
                .startTime(workShift.getStartTime())
                .endTime(workShift.getEndTime())
                .requirements(requirements)
                .missingRoles(new ArrayList<>(requirements)) // Initially all are missing
                .isCompliant(false)
                .assignments(new ArrayList<>())
                .build();
    }
    
    // Factory method for reconstitution
    public static WorkSchedule reconstitute(
            String id,
            LocalDate workDate,
            String workShiftId,
            String shiftName,
            LocalTime startTime,
            LocalTime endTime,
            boolean isCompliant,
            List<RoleRequirement> requirements,
            List<RoleRequirement> missingRoles,
            List<StaffAssignment> assignments
    ) {
        return WorkSchedule.builder()
                .id(id)
                .workDate(workDate)
                .workShiftId(workShiftId)
                .shiftName(shiftName)
                .startTime(startTime)
                .endTime(endTime)
                .isCompliant(isCompliant)
                .requirements(requirements != null ? requirements : new ArrayList<>())
                .missingRoles(missingRoles != null ? missingRoles : new ArrayList<>())
                .assignments(assignments != null ? assignments : new ArrayList<>())
                .build();
    }
    
    // Business logic: Add staff assignments
    public void addStaffAssignments(List<StaffAssignment> newAssignments) {
        for (StaffAssignment assignment : newAssignments) {
            // Check for duplicates
            boolean exists = assignments.stream()
                    .anyMatch(a -> a.getProfileId().equals(assignment.getProfileId()));
            
            if (!exists) {
                assignments.add(assignment);
            }
        }
        
        recalculateCompliance();
    }
    
    // Business logic: Remove staff assignments
    public void removeStaffAssignments(List<String> staffIds) {
        assignments.removeIf(a -> staffIds.contains(a.getProfileId()));
        recalculateCompliance();
    }
    
    // Check if staff is already assigned
    public boolean isStaffAssigned(String profileId) {
        return assignments.stream()
                .anyMatch(a -> a.getProfileId().equals(profileId));
    }
    
    // Recalculate compliance and missing roles
    private void recalculateCompliance() {
        // Count current staff by role
        Map<String, Long> currentCounts = assignments.stream()
                .collect(Collectors.groupingBy(
                        StaffAssignment::getAccountType,
                        Collectors.counting()
                ));
        
        // Calculate missing roles
        List<RoleRequirement> newMissing = new ArrayList<>();
        boolean allMet = true;
        
        for (RoleRequirement requirement : requirements) {
            long current = currentCounts.getOrDefault(requirement.getAccountType(), 0L);
            int needed = requirement.getQuantity();
            
            if (current < needed) {
                allMet = false;
                newMissing.add(RoleRequirement.of(
                        requirement.getAccountType(),
                        (int)(needed - current)
                ));
            }
        }
        
        this.missingRoles = newMissing;
        this.isCompliant = allMet;
    }
    
    // Check for time overlap with another schedule
    public boolean hasTimeOverlapWith(WorkSchedule other) {
        if (!this.workDate.equals(other.workDate)) {
            return false;
        }
        
        // Check if times overlap
        return this.startTime.isBefore(other.endTime) && this.endTime.isAfter(other.startTime);
    }
    
    // Get total hours for this schedule
    public long getDurationInHours() {
        return java.time.Duration.between(startTime, endTime).toHours();
    }
    
    // Nested value objects
    @Getter
    @Builder
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class RoleRequirement {
        String accountType;
        int quantity;
        
        public static RoleRequirement of(String accountType, int quantity) {
            return new RoleRequirement(accountType, quantity);
        }
    }
    
    @Getter
    @Builder
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class StaffAssignment {
        String profileId;
        String fullName;
        String accountType;
        String email;
        String phoneNumber;
        String status; // "Assigned"
        
        public static StaffAssignment create(
                String profileId,
                String fullName,
                String accountType,
                String email,
                String phoneNumber
        ) {
            return StaffAssignment.builder()
                    .profileId(profileId)
                    .fullName(fullName)
                    .accountType(accountType)
                    .email(email)
                    .phoneNumber(phoneNumber)
                    .status("Assigned")
                    .build();
        }
    }
}
