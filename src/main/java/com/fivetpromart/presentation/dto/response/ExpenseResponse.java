package com.fivetpromart.presentation.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ExpenseResponse(
        String id,
        String category,
        String description,
        String payDate,
        BigDecimal amount,
        String image
) {
}
