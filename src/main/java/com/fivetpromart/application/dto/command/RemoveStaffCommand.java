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
public class RemoveStaffCommand {
    
    List<LocalDate> workDates;
    List<String> workShiftIds;
    List<String> assignedStaffIds;
}
