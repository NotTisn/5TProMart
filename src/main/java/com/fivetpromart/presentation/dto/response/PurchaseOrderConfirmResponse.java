package com.fivetpromart.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class PurchaseOrderConfirmResponse {
    private String poCode;
    private String status;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate checkDate;

    private BigDecimal finalTotalAmount;
    private List<LotToPrintResponse> lotsToPrint;
}
