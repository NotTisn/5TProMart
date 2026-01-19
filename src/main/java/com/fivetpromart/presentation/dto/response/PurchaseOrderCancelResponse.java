package com.fivetpromart.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class PurchaseOrderCancelResponse {
    private String poCode;
    private String poId;
    private String status;
    private String cancellationReason;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate checkDate;
}
