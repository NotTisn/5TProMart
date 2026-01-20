package com.fivetpromart.application.dto.command;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssignStaffCommand {
    
    List<LocalDate> workDates;
    String workShiftId;
    List<String> assignedStaffIds;
}
