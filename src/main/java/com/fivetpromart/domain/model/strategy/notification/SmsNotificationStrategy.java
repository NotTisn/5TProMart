package com.fivetpromart.domain.model.strategy.notification;

import com.fivetpromart.domain.model.Order;
import lombok.extern.slf4j.Slf4j;

/**
 * SMS Notification Strategy
 * 
 * IMPLEMENTATION STATUS: LOGGING ONLY
 * This strategy currently logs notifications instead of sending actual SMS.
 * To integrate real SMS, inject an SMS gateway service (Twilio, AWS SNS, etc.)
 * through constructor injection and call it in the notify methods.
 * 
 * The Strategy Pattern is correctly implemented - just swap this for a real
 * implementation when ready.
 */
@Slf4j
public class SmsNotificationStrategy implements NotificationStrategy {
    
    private final boolean enabled;
    
    public SmsNotificationStrategy() {
        this(true);
    }
    
    public SmsNotificationStrategy(boolean enabled) {
        this.enabled = enabled;
    }
    
    @Override
    public void notifyOrderCreated(Order order) {
        if (!enabled) {
            return;
        }
        
        // PLACEHOLDER: Logs instead of sending SMS
        // Future: Inject SmsGateway and call smsGateway.send(phone, message)
        log.info("📱 [SMS] Sending order created notification for order: {}", order.getOrderId());
        log.info("   Message: Your order {} has been confirmed. Total: {} VND", 
                order.getOrderId(), order.getTotalAmount());
    }
    
    @Override
    public void notifyOrderCompleted(Order order) {
        if (!enabled) {
            return;
        }
        
        log.info("📱 [SMS] Sending order completed notification for order: {}", order.getOrderId());
    }
    
    @Override
    public void notifyOrderCancelled(Order order) {
        if (!enabled) {
            return;
        }
        
        log.info("📱 [SMS] Sending order cancelled notification for order: {}", order.getOrderId());
    }
    
    @Override
    public String getChannelName() {
        return "SMS";
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
