package com.fivetpromart.application.dto.command;

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
public class PurchaseOrderConfirmCommand {
    private String staffIdChecked;
    private LocalDate checkDate;
    private String notes;
    private InvoiceCommand invoice;
    private List<ActualItemCommand> actualItems;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvoiceCommand {
        private String invoiceNumber;
        private LocalDate invoiceDate;
        private List<String> images;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActualItemCommand {
        private String productId;
        private Long quantityReceived;
        private BigDecimal importPrice;
        private LocalDate manufactureDate;
        private LocalDate expirationDate;
        private String notes;
    }
}
