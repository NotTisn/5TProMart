package com.fivetpromart.infrastructure.persistence.workshift;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;

@Entity
@Table(name = "work_shifts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkShiftDbo {
    
    @Id
    @Column(name = "id")
    String id;
    
    @Column(name = "shift_name", nullable = false)
    String shiftName;
    
    @Column(name = "start_time", nullable = false)
    LocalTime startTime;
    
    @Column(name = "end_time", nullable = false)
    LocalTime endTime;
    
    @Column(name = "is_active")
    boolean isActive;
    
    @Column(name = "role_config_id")
    String roleConfigId;
    
    @Column(name = "role_config_name")
    String roleConfigName;
}
