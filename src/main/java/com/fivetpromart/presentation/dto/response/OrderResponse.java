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
public class OrderResponse {
    private String orderId;
    private String orderDate;  // dd-MM-yyyy HH:mm:ss format
    private String customerName;  // "Khách lẻ" if customerId is null
    private String staffName;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private String status;
    private String createdAt;  // dd-MM-yyyy HH:mm:ss format
}
