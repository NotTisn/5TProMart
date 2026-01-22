package com.fivetpromart.presentation.mapper;

import com.fivetpromart.domain.model.salary.SalaryReport;
import com.fivetpromart.domain.model.salary.SalaryRoleConfig;
import com.fivetpromart.domain.model.salary.StaffSalaryDetail;
import com.fivetpromart.presentation.dto.response.SalaryConfigResponse;
import com.fivetpromart.presentation.dto.response.SalaryReportResponse;
import com.fivetpromart.presentation.dto.response.StaffSalaryDetailResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.stream.Collectors;

@Component
public class SalaryMapper {
    
    public SalaryConfigResponse toResponse(SalaryRoleConfig config) {
        return SalaryConfigResponse.builder()
                .id(config.getId())
                .role(config.getRole())
                .hourlySalary(config.getHourlyRate())
                .updatedAt(config.getUpdatedAt().atZone(ZoneId.of("UTC")).toLocalDate())
                .build();
    }
    
    public SalaryReportResponse toResponse(SalaryReport report) {
        return SalaryReportResponse.builder()
                .range(SalaryReportResponse.DateRange.builder()
                        .startDate(report.getStartDate())
                        .endDate(report.getEndDate())
                        .build())
                .summary(SalaryReportResponse.Summary.builder()
                        .totalSalaryCost(report.getTotalSalaryCost())
                        .totalWorkHours(report.getTotalWorkHours())
                        .totalStaffs(report.getTotalStaffs())
                        .build())
                .staffSalaryDetails(report.getStaffDetails().stream()
                        .map(this::toStaffDetailResponse)
                        .collect(Collectors.toList()))
                .build();
    }
    
    private SalaryReportResponse.StaffSalaryDetail toStaffDetailResponse(SalaryReport.StaffSalaryDetail detail) {
        return SalaryReportResponse.StaffSalaryDetail.builder()
                .userId(detail.getUserId())
                .fullName(detail.getFullName())
                .role(detail.getRole())
                .totalWorkHours(detail.getTotalWorkHours())
                .totalSalary(detail.getTotalSalary())
                .build();
    }
    
    public StaffSalaryDetailResponse toResponse(StaffSalaryDetail detail) {
        return StaffSalaryDetailResponse.builder()
                .userId(detail.getUserId())
                .fullName(detail.getFullName())
                .role(detail.getRole())
                .range(StaffSalaryDetailResponse.DateRange.builder()
                        .fromDate(detail.getStartDate())
                        .toDate(detail.getEndDate())
                        .build())
                .summary(StaffSalaryDetailResponse.Summary.builder()
                        .totalSalary(detail.getTotalSalary())
                        .totalWorkHours(detail.getTotalWorkHours())
                        .build())
                .dailyDetails(detail.getDailyDetails().stream()
                        .map(this::toDailyDetailResponse)
                        .collect(Collectors.toList()))
                .build();
    }
    
    private StaffSalaryDetailResponse.DailyDetail toDailyDetailResponse(StaffSalaryDetail.DailyDetail detail) {
        return StaffSalaryDetailResponse.DailyDetail.builder()
                .date(detail.getDate())
                .workHours(detail.getWorkHours())
                .appliedRate(detail.getHourlyRate())
                .dailyAmount(detail.getDailyAmount())
                .build();
    }
}
