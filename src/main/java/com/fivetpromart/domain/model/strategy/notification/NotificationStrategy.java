package com.fivetpromart.domain.model.strategy.notification;

import com.fivetpromart.domain.model.Order;

/**
 * Strategy Pattern for Order Notifications
 * Allows different notification channels (Email, SMS, Push, etc.)
 */
public interface NotificationStrategy {
    
    /**
     * Send notification when order is created
     * @param order The created order
     */
    void notifyOrderCreated(Order order);
    
    /**
     * Send notification when order is completed
     * @param order The completed order
     */
    void notifyOrderCompleted(Order order);
    
    /**
     * Send notification when order is cancelled
     * @param order The cancelled order
     */
    void notifyOrderCancelled(Order order);
    
    /**
     * Get notification channel name
     * @return Channel name (EMAIL, SMS, PUSH, etc.)
     */
    String getChannelName();
    
    /**
     * Check if notification is enabled
     * @return true if enabled
     */
    boolean isEnabled();
}
