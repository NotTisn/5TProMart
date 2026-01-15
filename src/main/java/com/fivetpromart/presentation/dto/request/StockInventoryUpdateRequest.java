package com.fivetpromart.presentation.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockInventoryUpdateRequest {

    @Positive(message = "Stock quantity must be greater than 0")
    private Long stockQuantity;

    private String status;
}
