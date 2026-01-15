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
    private BigDecimal totalAmount;
    private BigDecimal changeReturned;
    private Long pointsEarned;
    private List<OrderItemInfo> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemInfo {
        private String productName;
        private String lotId;
        private Long quantity;
        private BigDecimal subTotal;
    }
}
