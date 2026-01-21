package com.fivetpromart.application.usecase;

import com.fivetpromart.application.dto.ExpenseDto;
import com.fivetpromart.application.dto.ExpenseReportDto;
import com.fivetpromart.application.dto.command.CreateExpenseCommand;
import com.fivetpromart.application.dto.command.UpdateExpenseCommand;
import com.fivetpromart.application.dto.query.GetExpenseReportQuery;
import com.fivetpromart.application.dto.query.GetExpensesQuery;
import com.fivetpromart.application.mapper.ExpenseDataMapper;
import com.fivetpromart.application.port.in.ExpenseUseCase;
import com.fivetpromart.application.port.out.ExpensePersistencePort;
import com.fivetpromart.domain.exception.ResourceNotFoundException;
import com.fivetpromart.domain.model.expense.Expense;
import com.fivetpromart.domain.model.expense.ExpenseId;
import com.fivetpromart.domain.model.expense.ExpenseReport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpenseUseCaseImpl implements ExpenseUseCase {

    private final ExpensePersistencePort expensePersistencePort;
    private final ExpenseDataMapper expenseDataMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ExpenseDto> getExpenses(GetExpensesQuery query) {
        log.info("Getting expenses with query: {}", query);
        
        Sort sort = Sort.by(
                "desc".equalsIgnoreCase(query.order()) ? Sort.Direction.DESC : Sort.Direction.ASC,
                query.sortBy() != null ? query.sortBy() : "payDate"
        );
        
        Pageable pageable = PageRequest.of(query.page(), query.size(), sort);
        
        Page<Expense> expenses = expensePersistencePort.findExpenses(
                query.startDate(),
                query.endDate(),
                query.search(),
                pageable
        );
        
        return expenses.map(expenseDataMapper::toDto);
    }

    @Override
    @Transactional
    public ExpenseDto createExpense(CreateExpenseCommand command) {
        log.info("Creating expense: {}", command);
        
        Expense expense = expenseDataMapper.toDomain(command);
        expense = expense.withId(ExpenseId.generate());
        expense.validate();
        
        Expense savedExpense = expensePersistencePort.save(expense);
        log.info("Expense created with ID: {}", savedExpense.getId().value());
        
        return expenseDataMapper.toDto(savedExpense);
    }

    @Override
    @Transactional
    public ExpenseDto updateExpense(UpdateExpenseCommand command) {
        log.info("Updating expense: {}", command.expenseId());
        
        ExpenseId expenseId = ExpenseId.of(command.expenseId());
        Expense existingExpense = expensePersistencePort.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with ID: " + command.expenseId()));
        
        Expense updatedExpense = expenseDataMapper.toDomain(command);
        updatedExpense.validate();
        
        Expense savedExpense = expensePersistencePort.save(updatedExpense);
        log.info("Expense updated: {}", savedExpense.getId().value());
        
        return expenseDataMapper.toDto(savedExpense);
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenseReportDto getCategoryReport(GetExpenseReportQuery query) {
        log.info("Generating expense report from {} to {}", query.startDate(), query.endDate());
        
        if (query.startDate() == null || query.endDate() == null) {
            throw new IllegalArgumentException("Start date and end date are required");
        }
        
        ExpenseReport report = expensePersistencePort.generateCategoryReport(
                query.startDate(),
                query.endDate()
        );
        
        return expenseDataMapper.toDto(report);
    }

    @Override
    @Transactional
    public ExpenseDto restoreExpense(String expenseId) {
        log.info("Restoring expense: {}", expenseId);
        
        Expense expense = expensePersistencePort.findByIdIncludingDeleted(new ExpenseId(expenseId))
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with ID: " + expenseId));
        
        if (expense.isActive()) {
            log.warn("Expense {} is already active", expenseId);
        }
        
        expense.activate();
        Expense saved = expensePersistencePort.save(expense);
        
        return expenseDataMapper.toDto(saved);
    }
}
