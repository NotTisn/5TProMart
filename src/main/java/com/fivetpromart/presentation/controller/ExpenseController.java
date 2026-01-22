package com.fivetpromart.presentation.controller;

import com.fivetpromart.application.dto.ExpenseDto;
import com.fivetpromart.application.dto.ExpenseReportDto;
import com.fivetpromart.application.dto.query.GetExpenseReportQuery;
import com.fivetpromart.application.dto.query.GetExpensesQuery;
import com.fivetpromart.application.port.in.ExpenseUseCase;
import com.fivetpromart.presentation.dto.request.CreateExpenseRequest;
import com.fivetpromart.presentation.dto.request.UpdateExpenseRequest;
import com.fivetpromart.presentation.dto.response.ApiResponse;
import com.fivetpromart.presentation.dto.response.ExpenseReportResponse;
import com.fivetpromart.presentation.dto.response.ExpenseResponse;
import com.fivetpromart.presentation.dto.response.PaginationMeta;
import com.fivetpromart.presentation.mapper.ExpensePresentationMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseUseCase expenseUseCase;
    private final ExpensePresentationMapper mapper;

    /**
     * 1.1 Get expenses query
     * GET /api/v1/expenses
     */
    @GetMapping
    @PreAuthorize("hasRole('Admin')")
    public ApiResponse<List<ExpenseResponse>> getExpenses(
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDate,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "false") Boolean includeDeleted,
            @RequestParam(defaultValue = "payDate") String sortBy,
            @RequestParam(defaultValue = "desc") String order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("Getting expenses list");

        // Build query
        GetExpensesQuery query = GetExpensesQuery.builder()
                .startDate(startDate)
                .endDate(endDate)
                .search(search)
                .includeDeleted(includeDeleted)
                .sortBy(sortBy)
                .order(order)
                .page(page)
                .size(size)
                .build();

        // Call use case
        Page<ExpenseDto> expensePage = expenseUseCase.getExpenses(query);

        // Map to response
        List<ExpenseResponse> responses = expensePage.getContent().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        // Build pagination
        PaginationMeta paginationMeta = PaginationMeta.builder()
                .totalItems(expensePage.getTotalElements())
                .itemsPerPage(expensePage.getSize())
                .totalPages(expensePage.getTotalPages())
                .startPage(expensePage.getNumber() + 1)
                .build();

        return ApiResponse.<List<ExpenseResponse>>builder()
                .success(true)
                .message("Get expenses successfully.")
                .data(responses)
                .pagination(paginationMeta)
                .build();
    }

    /**
     * 1.2 Create expense
     * POST /api/v1/expenses
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('Admin')")
    public ApiResponse<ExpenseResponse> createExpense(@Valid @RequestBody CreateExpenseRequest request) {
        log.info("Creating expense");

        // Call use case
        ExpenseDto expenseDto = expenseUseCase.createExpense(
                mapper.toCommand(request)
        );

        // Map to response
        ExpenseResponse response = mapper.toResponse(expenseDto);

        return ApiResponse.<ExpenseResponse>builder()
                .success(true)
                .message("Expense created.")
                .data(response)
                .build();
    }

    /**
     * 1.3 Update expense
     * PUT /api/v1/expenses/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ApiResponse<ExpenseResponse> updateExpense(
            @PathVariable String id,
            @Valid @RequestBody UpdateExpenseRequest request
    ) {
        log.info("Updating expense: {}", id);

        // Call use case
        ExpenseDto expenseDto = expenseUseCase.updateExpense(
                mapper.toCommand(id, request)
        );

        // Map to response
        ExpenseResponse response = mapper.toResponse(expenseDto);

        return ApiResponse.<ExpenseResponse>builder()
                .success(true)
                .message("Expense updated successfully.")
                .data(response)
                .build();
    }

    @PostMapping("/{id}/restore")
    @PreAuthorize("hasRole('Admin')")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<ExpenseResponse> restoreExpense(
            @PathVariable String id
    ) {
        log.info("Restoring expense: {}", id);

        ExpenseDto expenseDto = expenseUseCase.restoreExpense(id);

        return ApiResponse.<ExpenseResponse>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Successfully restored expense")
                .data(mapper.toResponse(expenseDto))
                .build();
    }

    /**
     * 1.4 Get expense category report
     * GET /api/v1/expenses/category-report
     */
    @GetMapping("/category-report")
    @PreAuthorize("hasRole('Admin')")
    public ApiResponse<ExpenseReportResponse> getCategoryReport(
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDate
    ) {
        log.info("Getting expense category report from {} to {}", startDate, endDate);

        // Build query
        GetExpenseReportQuery query = GetExpenseReportQuery.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();

        // Call use case
        ExpenseReportDto reportDto = expenseUseCase.getCategoryReport(query);

        // Map to response
        ExpenseReportResponse response = mapper.toResponse(reportDto);

        return ApiResponse.<ExpenseReportResponse>builder()
                .success(true)
                .message("Get expense report successfully.")
                .data(response)
                .build();
    }
}
