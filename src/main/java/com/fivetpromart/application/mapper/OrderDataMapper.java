package com.fivetpromart.application.mapper;

import com.fivetpromart.application.dto.OrderDto;
import com.fivetpromart.domain.model.Order;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderDataMapper {

    public OrderDto toDto(Order order) {
        return OrderDto.builder()
                .orderId(order.getOrderId())
                .orderDate(order.getOrderDate())
                .staffId(order.getStaffId())
                .customerId(order.getCustomerId())
                .paymentMethod(order.getPaymentMethod())
                .status(order.getStatus())
                .subTotal(order.getSubTotal())
                .discountAmount(order.getDiscountAmount())
                .originalAmount(order.getOriginalAmount())
                .roundingAdjustment(order.getRoundingAdjustment())
                .totalAmount(order.getTotalAmount())
                .amountGiven(order.getAmountGiven())
                .changeReturned(order.getChangeReturned())
                .pointsEarned(order.getPointsEarned())
                .items(order.getItems().stream()
                        .map(this::toItemDto)
                        .collect(Collectors.toList()))
                .build();
    }

    private OrderDto.OrderItemDto toItemDto(Order.OrderItem item) {
        return OrderDto.OrderItemDto.builder()
                .orderItemId(item.getOrderItemId())
                .orderId(item.getOrderId())
                .lotId(item.getLotId())
                .productId(item.getProductId())
                .productName(item.getProductName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .subTotal(item.getSubTotal())
                .build();
    }
}
