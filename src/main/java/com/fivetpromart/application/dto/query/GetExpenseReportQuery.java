package com.fivetpromart.application.dto.query;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record GetExpenseReportQuery(
        LocalDate startDate,
        LocalDate endDate
) {
}
