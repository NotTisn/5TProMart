package com.fivetpromart.domain.model.state;

import com.fivetpromart.domain.model.Order;

/**
 * Paid Order State
 * Order has been paid and completed
 */
public class PaidOrderState extends AbstractOrderState {
    
    @Override
    public void cancel(Order order) {
        // Can cancel a paid order (refund scenario)
        changeState(order, new CancelledOrderState());
    }
    
    @Override
    public String getStateName() {
        return "PAID";
    }
    
    @Override
    public boolean canTransitionTo(String targetState) {
        return "CANCELLED".equals(targetState);  // Only allow cancellation
    }
}
