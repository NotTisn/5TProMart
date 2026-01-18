package com.fivetpromart.domain.model.strategy.notification;

import com.fivetpromart.domain.model.Order;
import lombok.extern.slf4j.Slf4j;

/**
 * Email Notification Strategy
 * 
 * IMPLEMENTATION STATUS: LOGGING ONLY
 * This strategy currently logs notifications instead of sending actual emails.
 * To integrate real email, inject a mail service (Spring Mail, SendGrid, etc.)
 * through constructor injection and call it in the notify methods.
 * 
 * The Strategy Pattern is correctly implemented - just swap this for a real
 * implementation when ready.
 */
@Slf4j
public class EmailNotificationStrategy implements NotificationStrategy {
    
    private final boolean enabled;
    
    public EmailNotificationStrategy() {
        this(true);
    }
    
    public EmailNotificationStrategy(boolean enabled) {
        this.enabled = enabled;
    }
    
    @Override
    public void notifyOrderCreated(Order order) {
        if (!enabled) {
            return;
        }
        
        // PLACEHOLDER: Logs instead of sending email
        // Future: Inject MailService and call mailService.send(to, subject, body)
        log.info("📧 [EMAIL] Sending order created notification for order: {}", order.getOrderId());
        log.info("   To: Customer ID: {}", order.getCustomerId());
        log.info("   Subject: Order Confirmation - {}", order.getOrderId());
        log.info("   Total: {}", order.getTotalAmount());
    }
    
    @Override
    public void notifyOrderCompleted(Order order) {
        if (!enabled) {
            return;
        }
        
        log.info("📧 [EMAIL] Sending order completed notification for order: {}", order.getOrderId());
    }
    
    @Override
    public void notifyOrderCancelled(Order order) {
        if (!enabled) {
            return;
        }
        
        log.info("📧 [EMAIL] Sending order cancelled notification for order: {}", order.getOrderId());
    }
    
    @Override
    public String getChannelName() {
        return "EMAIL";
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
