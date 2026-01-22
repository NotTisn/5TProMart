package com.fivetpromart.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuyXGetYProductRequest {
    
    @NotBlank(message = "Product buy ID is required")
    private String productBuy;
    
    @NotBlank(message = "Product get ID is required")
    private String productGet;
}
