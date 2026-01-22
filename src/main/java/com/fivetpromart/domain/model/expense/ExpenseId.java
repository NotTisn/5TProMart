package com.fivetpromart.domain.model.expense;

import java.util.UUID;

public record ExpenseId(String value) {
    public ExpenseId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Expense ID cannot be null or blank");
        }
    }

    public static ExpenseId generate() {
        return new ExpenseId(UUID.randomUUID().toString());
    }

    public static ExpenseId of(String value) {
        return new ExpenseId(value);
    }
}
