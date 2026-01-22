package com.fivetpromart.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalaryReportResponse {
    
    private DateRange range;
    private Summary summary;
    private List<StaffSalaryDetail> staffSalaryDetails;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DateRange {
        @JsonFormat(pattern = "dd-MM-yyyy")
        private LocalDate startDate;
        
        @JsonFormat(pattern = "dd-MM-yyyy")
        private LocalDate endDate;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Summary {
        private BigDecimal totalSalaryCost;
        private Double totalWorkHours;
        private Integer totalStaffs;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StaffSalaryDetail {
        private String userId;
        private String fullName;
        private String role;
        private Double totalWorkHours;
        private BigDecimal totalSalary;
    }
}
