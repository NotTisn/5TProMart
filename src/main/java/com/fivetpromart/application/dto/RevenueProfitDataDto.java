package com.fivetpromart.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Builder
public class RevenueProfitDataDto {
    private Instant date;
    private BigDecimal revenue;
    private BigDecimal expense;
    private BigDecimal profit;
}
