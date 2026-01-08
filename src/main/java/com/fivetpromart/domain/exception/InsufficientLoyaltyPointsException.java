package com.fivetpromart.domain.exception;

/**
 * Thrown when trying to redeem more loyalty points than available.
 * HTTP: 400 Bad Request
 */
public class InsufficientLoyaltyPointsException extends DomainException {
    public InsufficientLoyaltyPointsException(long available, long requested) {
        super(String.format("Insufficient loyalty points. Available: %d, Requested: %d", available, requested));
    }
}
