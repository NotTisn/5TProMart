package com.fivetpromart.application.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record ExpenseReportDto(
        BigDecimal totalAmount,
        List<CategoryBreakdownDto> breakdown
) {
}
