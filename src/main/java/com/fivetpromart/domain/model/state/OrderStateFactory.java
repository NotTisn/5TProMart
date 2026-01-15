package com.fivetpromart.domain.model.state;

import com.fivetpromart.domain.exception.InvalidOrderException;

/**
 * Factory for creating Order State instances
 */
public class OrderStateFactory {
    
    /**
     * Create order state based on status string
     * @param status Status string (PENDING, PAID, CANCELLED)
     * @return OrderState implementation
     */
    public static OrderState createState(String status) {
        if (status == null || status.isBlank()) {
            throw new InvalidOrderException("Order status is required");
        }
        
        return switch (status.toUpperCase()) {
            case "PENDING", "UNPAID" -> new PendingOrderState();
            case "PAID", "COMPLETED" -> new PaidOrderState();
            case "CANCELLED" -> new CancelledOrderState();
            default -> throw new InvalidOrderException("Invalid order status: " + status);
        };
    }
}
