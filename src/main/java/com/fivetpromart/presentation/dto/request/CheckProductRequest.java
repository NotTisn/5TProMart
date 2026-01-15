package com.fivetpromart.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckProductRequest {

    @NotBlank(message = "Lot ID is required")
    private String lotId;

    @Positive(message = "Quantity must be positive")
    private Long quantity;
}
