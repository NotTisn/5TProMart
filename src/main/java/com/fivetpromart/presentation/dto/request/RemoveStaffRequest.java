package com.fivetpromart.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
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
public class RemoveStaffRequest {
    
    @NotEmpty(message = "Work dates cannot be empty")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @JsonProperty("workDates")
    List<LocalDate> workDates;
    
    @NotEmpty(message = "Work shift IDs cannot be empty")
    @JsonProperty("workShiftId")
    List<String> workShiftIds;
    
    @NotEmpty(message = "Assigned staff IDs cannot be empty")
    @JsonProperty("assignedStaffIds")
    List<String> assignedStaffIds;
}
