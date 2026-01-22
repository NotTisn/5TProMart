package com.fivetpromart.application.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderCancelCommand {
    private String staffIdChecked;
    private LocalDate checkDate;
    private String cancelNotesReason;
}
