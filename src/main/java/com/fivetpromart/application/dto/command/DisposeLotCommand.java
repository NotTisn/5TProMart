package com.fivetpromart.application.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DisposeLotCommand {
    private String lotId;
    private Long quantity;
    private String reason;
    private String notes;
    private String staffId;
}
