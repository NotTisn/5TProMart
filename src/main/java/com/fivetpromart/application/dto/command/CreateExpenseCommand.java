package com.fivetpromart.application.dto.command;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
public record CreateExpenseCommand(
        String category,
        String description,
        BigDecimal amount,
        LocalDate payDate,
        List<String> images
) {
}
