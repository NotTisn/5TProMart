package com.fivetpromart.domain.model.state;

import com.fivetpromart.domain.exception.InvalidOrderException;
import com.fivetpromart.domain.model.Order;

/**
 * Cancelled Order State
 * Order has been cancelled (terminal state)
 */
public class CancelledOrderState extends AbstractOrderState {
    
    @Override
    public void process(Order order) {
        throw new InvalidOrderException("Cannot process a cancelled order");
    }
    
    @Override
    public void cancel(Order order) {
        // Already cancelled - do nothing
    }
    
    @Override
    public void complete(Order order) {
        throw new InvalidOrderException("Cannot complete a cancelled order");
    }
    
    @Override
    public String getStateName() {
        return "CANCELLED";
    }
    
    @Override
    public boolean canTransitionTo(String targetState) {
        return false;  // Terminal state - no transitions allowed
    }
}
