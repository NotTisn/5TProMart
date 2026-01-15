package com.fivetpromart.domain.model.state;

import com.fivetpromart.domain.exception.InvalidOrderException;
import com.fivetpromart.domain.model.Order;

/**
 * Abstract base class for Order States
 * Provides common behavior and validation
 */
public abstract class AbstractOrderState implements OrderState {
    
    @Override
    public void process(Order order) {
        throw new InvalidOrderException(
                String.format("Cannot process order in %s state", getStateName())
        );
    }
    
    @Override
    public void cancel(Order order) {
        throw new InvalidOrderException(
                String.format("Cannot cancel order in %s state", getStateName())
        );
    }
    
    @Override
    public void complete(Order order) {
        throw new InvalidOrderException(
                String.format("Cannot complete order in %s state", getStateName())
        );
    }
    
    /**
     * Change the order's state
     * @param order The order to update
     * @param newState The new state
     */
    protected void changeState(Order order, OrderState newState) {
        order.setState(newState);
    }
}
