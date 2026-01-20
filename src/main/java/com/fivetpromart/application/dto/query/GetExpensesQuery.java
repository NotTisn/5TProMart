package com.fivetpromart.application.dto.query;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record GetExpensesQuery(
        LocalDate startDate,
        LocalDate endDate,
        String search,
        String sortBy,
        String order,
        int page,
        int size
) {
}
