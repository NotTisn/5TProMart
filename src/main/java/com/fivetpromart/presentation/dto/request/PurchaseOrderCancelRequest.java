package com.fivetpromart.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderCancelRequest {

    @NotNull(message = "Staff ID checked is required.")
    private String staffIdChecked;

    @NotNull(message = "Check date is required.")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate checkDate;

    @NotNull(message = "Cancellation reason is required.")
    private String cancelNotesReason;
}
