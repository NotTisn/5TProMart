package com.fivetpromart.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class AssignStaffRequest {
    
    @NotEmpty(message = "Work dates cannot be empty")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @JsonProperty("workDates")
    List<LocalDate> workDates;
    
    @NotBlank(message = "Work shift ID is required")
    @JsonProperty("workShiftId")
    String workShiftId;
    
    @NotEmpty(message = "Assigned staff IDs cannot be empty")
    @JsonProperty("assignedStaffIds")
    List<String> assignedStaffIds;
}
