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
public class OrderDetailResponse {
    private String orderId;
    private String orderDate;       // dd-MM-yyyy HH:mm:ss
    private String status;
    private String paymentMethod;
    private CustomerInfo customer;
    private StaffInfo staff;
    private List<OrderItemInfo> items;
    private BigDecimal subTotal;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private BigDecimal amountGiven;
    private BigDecimal changeReturned;
    private Long pointsEarned;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerInfo {
        private String customerId;
        private String fullName;
        private String phoneNumber;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StaffInfo {
        private String profileId;
        private String fullName;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemInfo {
        private String productId;
        private String productName;
        private Long quantity;
        private BigDecimal unitPrice;
        private BigDecimal subTotal;
    }
}
