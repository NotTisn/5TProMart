package com.fivetpromart.application.dto.query;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkScheduleSearchQuery {
    
    LocalDate startDate; // Required
    LocalDate endDate;   // Required
    String profileId;    // Optional - filter for specific staff
    String workShiftId;  // Optional - filter by shift
}
