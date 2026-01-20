package com.fivetpromart.infrastructure.persistence.expense.adapter;

import com.fivetpromart.application.port.out.ExpensePersistencePort;
import com.fivetpromart.domain.model.expense.CategoryBreakdown;
import com.fivetpromart.domain.model.expense.Expense;
import com.fivetpromart.domain.model.expense.ExpenseId;
import com.fivetpromart.domain.model.expense.ExpenseReport;
import com.fivetpromart.infrastructure.persistence.expense.entity.ExpenseDbo;
import com.fivetpromart.infrastructure.persistence.expense.mapper.ExpensePersistenceMapper;
import com.fivetpromart.infrastructure.persistence.expense.repository.ExpenseJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExpensePersistenceAdapter implements ExpensePersistencePort {

    private final ExpenseJpaRepository expenseJpaRepository;
    private final ExpensePersistenceMapper expensePersistenceMapper;

    @Override
    public Page<Expense> findExpenses(LocalDate startDate, LocalDate endDate, String search, Pageable pageable) {
        log.debug("Finding expenses with filters - startDate: {}, endDate: {}, search: {}", 
                startDate, endDate, search);
        
        Page<ExpenseDbo> expensePage = expenseJpaRepository.findExpensesByFilters(
                startDate, endDate, search, pageable
        );
        
        return expensePage.map(expensePersistenceMapper::toDomain);
    }

    @Override
    public Optional<Expense> findById(ExpenseId expenseId) {
        log.debug("Finding expense by ID: {}", expenseId.value());
        return expenseJpaRepository.findById(expenseId.value())
                .map(expensePersistenceMapper::toDomain);
    }

    @Override
    public Expense save(Expense expense) {
        log.debug("Saving expense: {}", expense.getId().value());
        ExpenseDbo dbo = expensePersistenceMapper.toDbo(expense);
        ExpenseDbo savedDbo = expenseJpaRepository.save(dbo);
        return expensePersistenceMapper.toDomain(savedDbo);
    }

    @Override
    public ExpenseReport generateCategoryReport(LocalDate startDate, LocalDate endDate) {
        log.debug("Generating category report from {} to {}", startDate, endDate);
        
        List<ExpenseJpaRepository.CategoryBreakdownProjection> projections = 
                expenseJpaRepository.findCategoryBreakdown(startDate, endDate);
        
        List<CategoryBreakdown> breakdowns = projections.stream()
                .map(p -> CategoryBreakdown.builder()
                        .categoryName(p.getCategoryName())
                        .totalAmount(p.getTotalAmount())
                        .build())
                .toList();
        
        BigDecimal totalAmount = breakdowns.stream()
                .map(CategoryBreakdown::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return ExpenseReport.builder()
                .totalAmount(totalAmount)
                .breakdown(breakdowns)
                .build();
    }
}
