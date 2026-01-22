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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderConfirmRequest {

    @NotNull(message = "Staff ID checked is required.")
    private String staffIdChecked;

    @NotNull(message = "Check date is required.")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate checkDate;

    private String notes;

    @Valid
    private InvoiceRequest invoice;

    @NotEmpty(message = "Received items cannot be empty.")
    @Valid
    private List<ActualItemRequest> actualItems;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvoiceRequest {
        private String invoiceNumber;

        @JsonFormat(pattern = "dd-MM-yyyy")
        private LocalDate invoiceDate;

        private List<String> images;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActualItemRequest {
        @NotNull(message = "Product ID is required.")
        private String productId;

        @NotNull(message = "Quantity received is required.")
        @Positive(message = "Quantity received must be greater than 0.")
        private Long quantityReceived;

        @NotNull(message = "Import price is required.")
        @Positive(message = "Import price must be greater than 0.")
        private BigDecimal importPrice;

        @NotNull(message = "Manufacture date is required.")
        @JsonFormat(pattern = "dd-MM-yyyy")
        private LocalDate manufactureDate;

        @NotNull(message = "Expiration date is required.")
        @JsonFormat(pattern = "dd-MM-yyyy")
        private LocalDate expirationDate;

        private String notes;
    }
}
