package com.fivetpromart.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReserveStockRequest(
        @NotBlank(message = "Lot ID is required")
        String lotId,
        
        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        Long quantity, // FIXED: Use Long to match StockInventory
        
        @NotBlank(message = "Reserved by is required (staff ID or session ID)")
        String reservedBy
) {
}
