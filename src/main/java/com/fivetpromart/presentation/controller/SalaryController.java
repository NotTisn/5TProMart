package com.fivetpromart.presentation.controller;

import com.fivetpromart.application.usecase.SalaryUseCase;
import com.fivetpromart.domain.model.salary.SalaryReport;
import com.fivetpromart.domain.model.salary.SalaryRoleConfig;
import com.fivetpromart.domain.model.salary.StaffSalaryDetail;
import com.fivetpromart.presentation.dto.request.CalculateDailySalaryRequest;
import com.fivetpromart.presentation.dto.request.UpdateSalaryConfigsRequest;
import com.fivetpromart.presentation.dto.response.*;
import com.fivetpromart.presentation.mapper.SalaryMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class SalaryController {

    private final SalaryUseCase salaryUseCase;
    private final SalaryMapper mapper;

    /**
     * 1. Get current salary configs
     * GET /api/v1/salary-configs
     */
    @GetMapping("/salary-configs")
    public ApiResponse<List<SalaryConfigResponse>> getAllSalaryConfigs() {
        log.info("Getting all salary configurations");

        List<SalaryRoleConfig> configs = salaryUseCase.getAllSalaryConfigs();
        List<SalaryConfigResponse> responses = configs.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        return ApiResponse.<List<SalaryConfigResponse>>builder()
                .success(true)
                .message("Get salary config successfully.")
                .data(responses)
                .build();
    }

    /**
     * 2. Update salary configs
     * PUT /api/v1/salary-configs
     */
    @PutMapping("/salary-configs")
    public ApiResponse<List<SalaryConfigResponse>> updateSalaryConfigs(
            @Valid @RequestBody UpdateSalaryConfigsRequest request
    ) {
        log.info("Updating salary configurations: {}", request);

        try {
            // Convert request to map
            Map<String, BigDecimal> roleRates = request.getConfigs().stream()
                    .collect(Collectors.toMap(
                            config -> config.getRole(),
                            config -> config.getHourlyRate()
                    ));

            List<SalaryRoleConfig> updatedConfigs = salaryUseCase.updateSalaryConfigs(roleRates);
            List<SalaryConfigResponse> responses = updatedConfigs.stream()
                    .map(mapper::toResponse)
                    .collect(Collectors.toList());

            return ApiResponse.<List<SalaryConfigResponse>>builder()
                    .success(true)
                    .message("Salary configs updated successfully.")
                    .data(responses)
                    .build();

        } catch (IllegalArgumentException e) {
            log.error("Validation error: {}", e.getMessage());
            return ApiResponse.<List<SalaryConfigResponse>>builder()
                    .success(false)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    /**
     * 3. Calculate Daily Salary
     * POST /api/v1/salary/daily-salary
     */
    @PostMapping("/salary/daily-salary")
    public ApiResponse<DailySalaryCalculationResponse> calculateDailySalary(
            @Valid @RequestBody CalculateDailySalaryRequest request
    ) {
        log.info("Calculating daily salary for date: {}", request.getDate());

        try {
            salaryUseCase.calculateDailySalary(request.getDate());

            DailySalaryCalculationResponse response = DailySalaryCalculationResponse.builder()
                    .processedDate(request.getDate())
                    .status("SUCCESS")
                    .build();

            return ApiResponse.<DailySalaryCalculationResponse>builder()
                    .success(true)
                    .message("Daily salary calculation completed.")
                    .data(response)
                    .build();

        } catch (IllegalArgumentException e) {
            log.error("Validation error: {}", e.getMessage());
            return ApiResponse.<DailySalaryCalculationResponse>builder()
                    .success(false)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    /**
     * 4. Get salaries query (report)
     * GET /api/v1/salary/salary-reports
     */
    @GetMapping("/salary/salary-reports")
    public ApiResponse<SalaryReportResponse> getSalaryReport(
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDate
    ) {
        log.info("Getting salary report from {} to {}", startDate, endDate);

        try {
            SalaryReport report = salaryUseCase.getSalaryReport(startDate, endDate);
            SalaryReportResponse response = mapper.toResponse(report);

            return ApiResponse.<SalaryReportResponse>builder()
                    .success(true)
                    .message("Get salary report succesfully.")
                    .data(response)
                    .build();

        } catch (IllegalArgumentException e) {
            log.error("Validation error: {}", e.getMessage());
            return ApiResponse.<SalaryReportResponse>builder()
                    .success(false)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    /**
     * 5. Get salary by staff
     * GET /api/v1/salary/salary-reports/{id}
     */
    @GetMapping("/salary/salary-reports/{id}")
    public ApiResponse<StaffSalaryDetailResponse> getStaffSalaryDetail(
            @PathVariable String id,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDate
    ) {
        log.info("Getting salary detail for staff {} from {} to {}", id, startDate, endDate);

        try {
            StaffSalaryDetail detail = salaryUseCase.getStaffSalaryDetail(id, startDate, endDate);
            StaffSalaryDetailResponse response = mapper.toResponse(detail);

            return ApiResponse.<StaffSalaryDetailResponse>builder()
                    .success(true)
                    .message("Get staff salary sucessfully")
                    .data(response)
                    .build();

        } catch (IllegalArgumentException e) {
            log.error("Validation error: {}", e.getMessage());
            return ApiResponse.<StaffSalaryDetailResponse>builder()
                    .success(false)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }
}
