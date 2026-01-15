package com.fivetpromart.infrastructure.persistence.jpa.mapper;

import com.fivetpromart.domain.model.Order;
import com.fivetpromart.infrastructure.persistence.jpa.entity.OrderDbo;
import com.fivetpromart.infrastructure.persistence.jpa.entity.OrderItemDbo;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderPersistenceMapper {

    /**
     * Convert domain Order to database OrderDbo
     */
    default OrderDbo toDbo(Order order) {
        if (order == null) {
            return null;
        }

        OrderDbo dbo = OrderDbo.builder()
                .orderId(order.getOrderId())
                .orderDate(order.getOrderDate())
                .staffId(order.getStaffId())
                .customerId(order.getCustomerId())
                .paymentMethod(order.getPaymentMethod())
                .status(order.getStatus())
                .subTotal(order.getSubTotal())
                .discountAmount(order.getDiscountAmount())
                .totalAmount(order.getTotalAmount())
                .amountGiven(order.getAmountGiven())
                .changeReturned(order.getChangeReturned())
                .pointsEarned(order.getPointsEarned())
                .build();

        // Convert items
        if (order.getItems() != null) {
            List<OrderItemDbo> itemDbos = order.getItems().stream()
                    .map(item -> toItemDbo(item, dbo))
                    .collect(Collectors.toList());
            dbo.setItems(itemDbos);
        }

        return dbo;
    }

    /**
     * Convert database OrderDbo to domain Order
     */
    default Order toDomain(OrderDbo dbo) {
        if (dbo == null) {
            return null;
        }

        // Convert items
        List<Order.OrderItem> items = null;
        if (dbo.getItems() != null) {
            items = dbo.getItems().stream()
                    .map(this::toItemDomain)
                    .collect(Collectors.toList());
        }

        return Order.reconstitute(
                dbo.getOrderId(),
                dbo.getOrderDate(),
                dbo.getStaffId(),
                dbo.getCustomerId(),
                dbo.getPaymentMethod(),
                dbo.getStatus(),
                dbo.getSubTotal(),
                dbo.getDiscountAmount(),
                dbo.getTotalAmount(),
                dbo.getAmountGiven(),
                dbo.getChangeReturned(),
                dbo.getPointsEarned(),
                items
        );
    }

    /**
     * Convert domain OrderItem to database OrderItemDbo
     */
    default OrderItemDbo toItemDbo(Order.OrderItem item, OrderDbo orderDbo) {
        if (item == null) {
            return null;
        }

        return OrderItemDbo.builder()
                .orderItemId(item.getOrderItemId())
                .order(orderDbo)
                .lotId(item.getLotId())
                .productId(item.getProductId())
                .productName(item.getProductName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .subTotal(item.getSubTotal())
                .build();
    }

    /**
     * Convert database OrderItemDbo to domain OrderItem
     */
    default Order.OrderItem toItemDomain(OrderItemDbo dbo) {
        if (dbo == null) {
            return null;
        }

        return Order.OrderItem.reconstitute(
                dbo.getOrderItemId(),
                dbo.getOrder() != null ? dbo.getOrder().getOrderId() : null,
                dbo.getLotId(),
                dbo.getProductId(),
                dbo.getProductName(),
                dbo.getQuantity(),
                dbo.getUnitPrice(),
                dbo.getSubTotal()
        );
    }
}
