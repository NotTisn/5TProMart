package com.fivetpromart.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class PurchaseOrderDetailResponse {
    private String _id;
    private String poCode;
    private String status;
    private String notes;
    private SupplierInfoResponse supplier;
    private String staffIdCreated;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate purchaseDate;

    private String staffIdChecked;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate checkDate;

    private InvoiceResponse invoice;

    private List<PurchaseOrderItemResponse> items;
    private BigDecimal totalAmount;
    private List<String> generatedLotIds;

    @Getter
    @Setter
    @Builder
    public static class PurchaseOrderItemResponse {
        private String productId;
        private String productName;
        private BigDecimal importPrice;
        private Long quantityOrdered;
        private Long quantityReceived;
        private BigDecimal subTotal;
    }
}
