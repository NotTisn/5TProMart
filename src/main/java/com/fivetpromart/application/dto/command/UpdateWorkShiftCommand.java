package com.fivetpromart.application.dto.command;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateWorkShiftCommand {
    String id;
    String shiftName;
    LocalTime startTime;
    LocalTime endTime;
    String roleConfigId;
}
