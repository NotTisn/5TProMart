package com.fivetpromart.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailySalaryCalculationResponse {
    
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate processedDate;
    
    private String status;
}
