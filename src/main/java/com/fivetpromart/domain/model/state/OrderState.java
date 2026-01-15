package com.fivetpromart.domain.model.state;

import com.fivetpromart.domain.model.Order;

/**
 * State Pattern for Order Status
 * Manages order lifecycle and valid state transitions
 */
public interface OrderState {
    
    /**
     * Process the order (move to next state)
     * @param order The order to process
     */
    void process(Order order);
    
    /**
     * Cancel the order
     * @param order The order to cancel
     */
    void cancel(Order order);
    
    /**
     * Complete the order (mark as paid)
     * @param order The order to complete
     */
    void complete(Order order);
    
    /**
     * Get the current state name
     * @return State name (PENDING, PAID, CANCELLED)
     */
    String getStateName();
    
    /**
     * Check if transition to another state is allowed
     * @param targetState Target state name
     * @return true if transition is allowed
     */
    boolean canTransitionTo(String targetState);
}
