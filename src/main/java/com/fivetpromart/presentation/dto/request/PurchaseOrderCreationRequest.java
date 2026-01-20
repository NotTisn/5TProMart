package com.fivetpromart.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderCreationRequest {

    @NotNull(message = "Supplier is required.")
    private String supplierId;

    private String notes;

    @NotEmpty(message = "Product list can't empty")
    @Valid
    private List<PurchaseOrderItemRequest> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PurchaseOrderItemRequest {
        @NotNull(message = "Product ID is required.")
        private String productId;

        @NotNull(message = "Product quantity is required.")
        @Positive(message = "Product quantity must be greater than 0.")
        private Long quantityOrdered;
    }
}
