package com.fivetpromart.domain.model.statistics;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Builder
public class RevenueProfitData {
    private LocalDate date;
    private BigDecimal revenue;
    private BigDecimal expense;
    private BigDecimal profit;
}
