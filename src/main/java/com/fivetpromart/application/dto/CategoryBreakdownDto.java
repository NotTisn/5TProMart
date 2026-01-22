package com.fivetpromart.application.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CategoryBreakdownDto(
        String categoryName,
        BigDecimal totalAmount
) {
}
