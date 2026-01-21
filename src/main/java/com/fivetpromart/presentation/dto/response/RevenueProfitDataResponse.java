package com.fivetpromart.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class RevenueProfitDataResponse {
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;
    private BigDecimal revenue;
    private BigDecimal expense;
    private BigDecimal profit;
}
