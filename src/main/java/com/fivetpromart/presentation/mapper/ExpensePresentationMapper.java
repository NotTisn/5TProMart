package com.fivetpromart.presentation.mapper;

import com.fivetpromart.application.dto.CategoryBreakdownDto;
import com.fivetpromart.application.dto.ExpenseDto;
import com.fivetpromart.application.dto.ExpenseReportDto;
import com.fivetpromart.application.dto.command.CreateExpenseCommand;
import com.fivetpromart.application.dto.command.UpdateExpenseCommand;
import com.fivetpromart.presentation.dto.request.CreateExpenseRequest;
import com.fivetpromart.presentation.dto.request.UpdateExpenseRequest;
import com.fivetpromart.presentation.dto.response.CategoryBreakdownResponse;
import com.fivetpromart.presentation.dto.response.ExpenseReportResponse;
import com.fivetpromart.presentation.dto.response.ExpenseResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExpensePresentationMapper {

    DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Mapping(target = "payDate", expression = "java(formatDate(dto.payDate()))")
    @Mapping(target = "image", expression = "java(joinImages(dto.images()))")
    ExpenseResponse toResponse(ExpenseDto dto);

    List<ExpenseResponse> toResponseList(List<ExpenseDto> dtos);

    @Mapping(target = "payDate", expression = "java(parseDate(request.payDate()))")
    @Mapping(target = "images", source = "image")
    CreateExpenseCommand toCommand(CreateExpenseRequest request);

    UpdateExpenseCommand toCommand(String expenseId, UpdateExpenseRequest request);

    CategoryBreakdownResponse toResponse(CategoryBreakdownDto dto);

    List<CategoryBreakdownResponse> toBreakdownResponseList(List<CategoryBreakdownDto> dtos);

    @Mapping(target = "breakdown", source = "breakdown")
    ExpenseReportResponse toResponse(ExpenseReportDto dto);

    default String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : null;
    }

    default LocalDate parseDate(String dateStr) {
        return dateStr != null ? LocalDate.parse(dateStr, DATE_FORMATTER) : null;
    }

    default String joinImages(List<String> images) {
        return images != null && !images.isEmpty() ? images.get(0) : null;
    }
}
