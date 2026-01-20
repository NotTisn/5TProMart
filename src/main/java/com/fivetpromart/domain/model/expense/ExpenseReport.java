package com.fivetpromart.domain.model.expense;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class ExpenseReport {
    private final BigDecimal totalAmount;
    private final List<CategoryBreakdown> breakdown;

    public void validate() {
        if (totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total amount cannot be negative");
        }
        if (breakdown == null) {
            throw new IllegalArgumentException("Breakdown cannot be null");
        }
        breakdown.forEach(CategoryBreakdown::validate);
    }
}
