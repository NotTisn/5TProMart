package com.fivetpromart.application.mapper;

import com.fivetpromart.application.dto.CategoryBreakdownDto;
import com.fivetpromart.application.dto.ExpenseDto;
import com.fivetpromart.application.dto.ExpenseReportDto;
import com.fivetpromart.application.dto.command.CreateExpenseCommand;
import com.fivetpromart.application.dto.command.UpdateExpenseCommand;
import com.fivetpromart.domain.model.expense.CategoryBreakdown;
import com.fivetpromart.domain.model.expense.Expense;
import com.fivetpromart.domain.model.expense.ExpenseId;
import com.fivetpromart.domain.model.expense.ExpenseReport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExpenseDataMapper {

    @Mapping(target = "id", expression = "java(expense.getId().value())")
    ExpenseDto toDto(Expense expense);

    List<ExpenseDto> toDtoList(List<Expense> expenses);

    @Mapping(target = "id", ignore = true)
    Expense toDomain(CreateExpenseCommand command);

    @Mapping(target = "id", expression = "java(com.fivetpromart.domain.model.expense.ExpenseId.of(command.expenseId()))")
    Expense toDomain(UpdateExpenseCommand command);

    CategoryBreakdownDto toDto(CategoryBreakdown breakdown);

    List<CategoryBreakdownDto> toBreakdownDtoList(List<CategoryBreakdown> breakdowns);

    ExpenseReportDto toDto(ExpenseReport report);

    default ExpenseId map(String value) {
        return value != null ? ExpenseId.of(value) : null;
    }

    default String map(ExpenseId expenseId) {
        return expenseId != null ? expenseId.value() : null;
    }
}
