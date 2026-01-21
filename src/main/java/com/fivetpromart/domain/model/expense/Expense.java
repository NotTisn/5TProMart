package com.fivetpromart.domain.model.expense;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder(toBuilder = true)
public class Expense {
    private final ExpenseId id;
    private final String category;
    private final String description;
    private final BigDecimal amount;
    private final LocalDate payDate;
    private final List<String> images;
    @Builder.Default
    private final Boolean isActive = true;

    public void validate() {
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Category cannot be null or blank");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (payDate == null) {
            throw new IllegalArgumentException("Pay date cannot be null");
        }
    }

    public Expense withId(ExpenseId id) {
        return this.toBuilder().id(id).build();
    }

    public Expense withCategory(String category) {
        return this.toBuilder().category(category).build();
    }

    public Expense withDescription(String description) {
        return this.toBuilder().description(description).build();
    }

    public Expense withAmount(BigDecimal amount) {
        return this.toBuilder().amount(amount).build();
    }

    public Expense withPayDate(LocalDate payDate) {
        return this.toBuilder().payDate(payDate).build();
    }

    public Expense withImages(List<String> images) {
        return this.toBuilder().images(images).build();
    }

    // Soft delete methods
    public Expense deactivate() {
        return this.toBuilder().isActive(false).build();
    }

    public Expense activate() {
        return this.toBuilder().isActive(true).build();
    }

    public boolean isActive() {
        return this.isActive != null && this.isActive;
    }
}
