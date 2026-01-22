package com.fivetpromart.application.port.out;

import com.fivetpromart.domain.model.expense.Expense;
import com.fivetpromart.domain.model.expense.ExpenseId;
import com.fivetpromart.domain.model.expense.ExpenseReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface ExpensePersistencePort {
    Page<Expense> findExpenses(LocalDate startDate, LocalDate endDate, String search, Pageable pageable);
    Optional<Expense> findById(ExpenseId expenseId);
    Optional<Expense> findByIdIncludingDeleted(ExpenseId expenseId);
    Expense save(Expense expense);
    ExpenseReport generateCategoryReport(LocalDate startDate, LocalDate endDate);
}
