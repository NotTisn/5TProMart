package com.fivetpromart.application.port.in;

import com.fivetpromart.application.dto.ExpenseDto;
import com.fivetpromart.application.dto.ExpenseReportDto;
import com.fivetpromart.application.dto.command.CreateExpenseCommand;
import com.fivetpromart.application.dto.command.UpdateExpenseCommand;
import com.fivetpromart.application.dto.query.GetExpenseReportQuery;
import com.fivetpromart.application.dto.query.GetExpensesQuery;
import org.springframework.data.domain.Page;

public interface ExpenseUseCase {
    Page<ExpenseDto> getExpenses(GetExpensesQuery query);
    ExpenseDto createExpense(CreateExpenseCommand command);
    ExpenseDto updateExpense(UpdateExpenseCommand command);
    ExpenseReportDto getCategoryReport(GetExpenseReportQuery query);
}
