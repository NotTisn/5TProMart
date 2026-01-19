package com.fivetpromart.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreationResponse {
    private String orderId;
    private String orderDate;       // dd-MM-yyyy HH:mm:ss
    private BigDecimal originalAmount;      // Total before rounding (for cash payments)
    private BigDecimal roundingAdjustment;  // Rounding amount (positive = rounded up, negative = rounded down)
    private BigDecimal totalAmount;         // Final total after rounding
    private BigDecimal changeReturned;
    private Long pointsEarned;
    private List<OrderItemInfo> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemInfo {
        private String productId;
        private String productName;
        private String lotId;
        private Long quantity;
        private BigDecimal subTotal;
    }
}
