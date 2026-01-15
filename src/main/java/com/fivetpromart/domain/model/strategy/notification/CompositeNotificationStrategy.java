package com.fivetpromart.domain.model.strategy.notification;

import com.fivetpromart.domain.model.Order;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Composite Notification Strategy (Composite Pattern)
 * Sends notifications through multiple channels simultaneously
 */
@Slf4j
public class CompositeNotificationStrategy implements NotificationStrategy {
    
    private final List<NotificationStrategy> strategies;
    
    public CompositeNotificationStrategy() {
        this.strategies = new ArrayList<>();
    }
    
    /**
     * Add a notification strategy
     * @param strategy Strategy to add
     */
    public void addStrategy(NotificationStrategy strategy) {
        if (strategy != null && strategy.isEnabled()) {
            strategies.add(strategy);
        }
    }
    
    /**
     * Remove a notification strategy
     * @param strategy Strategy to remove
     */
    public void removeStrategy(NotificationStrategy strategy) {
        strategies.remove(strategy);
    }
    
    @Override
    public void notifyOrderCreated(Order order) {
        log.info("📢 [COMPOSITE] Notifying order created through {} channels", strategies.size());
        strategies.forEach(strategy -> {
            try {
                strategy.notifyOrderCreated(order);
            } catch (Exception e) {
                log.error("Failed to send notification via {}: {}", 
                        strategy.getChannelName(), e.getMessage());
            }
        });
    }
    
    @Override
    public void notifyOrderCompleted(Order order) {
        log.info("📢 [COMPOSITE] Notifying order completed through {} channels", strategies.size());
        strategies.forEach(strategy -> {
            try {
                strategy.notifyOrderCompleted(order);
            } catch (Exception e) {
                log.error("Failed to send notification via {}: {}", 
                        strategy.getChannelName(), e.getMessage());
            }
        });
    }
    
    @Override
    public void notifyOrderCancelled(Order order) {
        log.info("📢 [COMPOSITE] Notifying order cancelled through {} channels", strategies.size());
        strategies.forEach(strategy -> {
            try {
                strategy.notifyOrderCancelled(order);
            } catch (Exception e) {
                log.error("Failed to send notification via {}: {}", 
                        strategy.getChannelName(), e.getMessage());
            }
        });
    }
    
    @Override
    public String getChannelName() {
        return "COMPOSITE";
    }
    
    @Override
    public boolean isEnabled() {
        return !strategies.isEmpty();
    }
}
