package com.fivetpromart.domain.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String orderId) {
        super("Order not found with ID: " + orderId);
    }
}
