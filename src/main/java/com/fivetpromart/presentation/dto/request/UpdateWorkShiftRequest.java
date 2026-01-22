package com.fivetpromart.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateWorkShiftRequest {
    @NotBlank(message = "Shift name is required")
    @JsonProperty("shiftName")
    String shiftName;

    @NotNull(message = "Start time is required")
    @JsonFormat(pattern = "HH:mm")
    @JsonProperty("startTime")
    LocalTime startTime;

    @NotNull(message = "End time is required")
    @JsonFormat(pattern = "HH:mm")
    @JsonProperty("endTime")
    LocalTime endTime;

    @NotBlank(message = "Role config ID is required")
    @JsonProperty("roleConfigId")
    String roleConfigId;

    @JsonProperty("isActive")
    Boolean isActive;
