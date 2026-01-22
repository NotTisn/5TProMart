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
public class StaffSalaryDetailResponse {
    
    private String userId;
    private String fullName;
    private String role;
    private DateRange range;
    private Summary summary;
    private List<DailyDetail> dailyDetails;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DateRange {
        @JsonFormat(pattern = "dd-MM-yyyy")
        private LocalDate fromDate;
        
        @JsonFormat(pattern = "dd-MM-yyyy")
        private LocalDate toDate;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Summary {
        private BigDecimal totalSalary;
        private Double totalWorkHours;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyDetail {
        @JsonFormat(pattern = "dd-MM-yyyy")
        private LocalDate date;
        
        private Double workHours;
        private BigDecimal appliedRate;
        private BigDecimal dailyAmount;
    }
}
