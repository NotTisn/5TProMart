package com.fivetpromart.presentation.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CategoryBreakdownResponse(
        String categoryName,
        BigDecimal totalAmount
) {
}
