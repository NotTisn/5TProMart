package com.fivetpromart.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private String orderId;
    private LocalDateTime orderDate;
    private String staffId;
    private String customerId;
    private String paymentMethod;
    private String status;
    private BigDecimal subTotal;
    private BigDecimal discountAmount;
    private BigDecimal originalAmount;      // Total before rounding (for cash payments)
    private BigDecimal roundingAdjustment;  // Rounding amount (positive = rounded up, negative = rounded down)
    private BigDecimal totalAmount;         // Final total after rounding
    private BigDecimal amountGiven;
    private BigDecimal changeReturned;
    private Long pointsEarned;
    private List<OrderItemDto> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemDto {
        private String orderItemId;
        private String orderId;
        private String lotId;
        private String productId;
        private String productName;
        private Long quantity;
        private BigDecimal unitPrice;
        private BigDecimal subTotal;
    }
}
