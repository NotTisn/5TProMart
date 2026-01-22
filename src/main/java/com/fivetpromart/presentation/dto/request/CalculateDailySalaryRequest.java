package com.fivetpromart.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculateDailySalaryRequest {
    
    @NotNull(message = "Date is required")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;
}
