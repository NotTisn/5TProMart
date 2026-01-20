package com.fivetpromart.application.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
public record ExpenseDto(
        String id,
        String category,
        String description,
        BigDecimal amount,
        LocalDate payDate,
        List<String> images
) {
}
