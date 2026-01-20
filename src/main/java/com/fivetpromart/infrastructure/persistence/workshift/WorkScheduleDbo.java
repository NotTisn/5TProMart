package com.fivetpromart.infrastructure.persistence.workshift;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "work_schedules")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkScheduleDbo {
    
    @Id
    @Column(name = "id")
    String id;
    
    @Column(name = "work_date", nullable = false)
    LocalDate workDate;
    
    @Column(name = "work_shift_id")
    String workShiftId;
    
    @Column(name = "shift_name")
    String shiftName;
    
    @Column(name = "start_time")
    LocalTime startTime;
    
    @Column(name = "end_time")
    LocalTime endTime;
    
    @Column(name = "is_compliant")
    boolean isCompliant;
    
    @ElementCollection
    @CollectionTable(
            name = "schedule_requirements",
            joinColumns = @JoinColumn(name = "schedule_id")
    )
    @Builder.Default
    List<RoleRequirementDbo> requirements = new ArrayList<>();
    
    @ElementCollection
    @CollectionTable(
            name = "schedule_missing_roles",
            joinColumns = @JoinColumn(name = "schedule_id")
    )
    @Builder.Default
    List<RoleRequirementDbo> missingRoles = new ArrayList<>();
    
    @ElementCollection
    @CollectionTable(
            name = "schedule_assignments",
            joinColumns = @JoinColumn(name = "schedule_id")
    )
    @Builder.Default
    List<StaffAssignmentDbo> assignments = new ArrayList<>();
}
