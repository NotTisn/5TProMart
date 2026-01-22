package com.fivetpromart.presentation.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record ExpenseReportResponse(
        BigDecimal totalAmount,
        List<CategoryBreakdownResponse> breakdown
) {
}
