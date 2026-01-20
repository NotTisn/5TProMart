package com.fivetpromart.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderItemResponse {
    private String productId;
    private String productName;
    private BigDecimal importPrice;
    private Integer quantityOrdered;
    private Integer quantityReceived;
    private BigDecimal subTotal;
}

