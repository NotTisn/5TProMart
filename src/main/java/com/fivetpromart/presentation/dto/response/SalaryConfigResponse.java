package com.fivetpromart.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalaryConfigResponse {
    
    private String id;
    private String role;
    private BigDecimal hourlySalary;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate updatedAt;
}
