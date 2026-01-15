package com.fivetpromart.domain.model.state;

import com.fivetpromart.domain.model.Order;

/**
 * Pending Order State
 * Order is created but not yet paid
 */
public class PendingOrderState extends AbstractOrderState {
    
    @Override
    public void complete(Order order) {
        // Transition to PAID state
        changeState(order, new PaidOrderState());
    }
    
    @Override
    public void cancel(Order order) {
        // Transition to CANCELLED state
        changeState(order, new CancelledOrderState());
    }
    
    @Override
    public String getStateName() {
        return "PENDING";
    }
    
    @Override
    public boolean canTransitionTo(String targetState) {
        return "PAID".equals(targetState) || "CANCELLED".equals(targetState);
    }
}
