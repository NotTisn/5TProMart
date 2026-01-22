package com.fivetpromart.infrastructure.persistence.expense.mapper;

import com.fivetpromart.domain.model.expense.Expense;
import com.fivetpromart.domain.model.expense.ExpenseId;
import com.fivetpromart.infrastructure.persistence.expense.entity.ExpenseDbo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExpensePersistenceMapper {

    @Mapping(target = "id", expression = "java(mapToExpenseId(dbo.getExpenseId()))")
    Expense toDomain(ExpenseDbo dbo);

    List<Expense> toDomainList(List<ExpenseDbo> dbos);

    @Mapping(target = "expenseId", expression = "java(mapToString(expense.getId()))")
    ExpenseDbo toDbo(Expense expense);

    default ExpenseId mapToExpenseId(String value) {
        return value != null ? ExpenseId.of(value) : null;
    }

    default String mapToString(ExpenseId expenseId) {
        return expenseId != null ? expenseId.value() : null;
    }
}
