package com.fivetpromart.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
public class PurchaseOrderDto {
    private String id;
    private String poCode;
    private SupplierInfoDto supplier;
    private String staffIdCreated;
    private String staffNameCreated;
    private String staffIdChecked;
    private String status;
    private String notes;
    private String cancellationReason;
    private BigDecimal totalAmount;
    private LocalDate purchaseDate;
    private LocalDate checkDate;
    private InvoiceDto invoice;
    private List<PurchaseOrderItemDto> items;
    private List<String> generatedLotIds;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class PurchaseOrderItemDto {
        private String itemId;
        private String productId;
        private String productName;
        private BigDecimal importPrice;
        private Long quantityOrdered;
        private Long quantityReceived;
        private BigDecimal subTotal;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class SupplierInfoDto {
        private String supplierId;
        private String supplierName;
        private String phone;
        private String representName;
        private String representPhoneNumber;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class InvoiceDto {
        private String invoiceNumber;
        private LocalDate invoiceDate;
        private List<String> images;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class LotToPrintDto {
        private String lotId;
        private String productName;
        private Long quantity;
        private LocalDate expirationDate;
        private String notes;
    }
}
