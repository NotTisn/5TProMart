package com.fivetpromart.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSalaryConfigsRequest {
    
    @NotEmpty(message = "Configs list cannot be empty")
    @Valid
    private List<SalaryConfigItemRequest> configs;
}
