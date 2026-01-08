package com.fivetpromart.domain.exception;

/**
 * Thrown when trying to reduce more stock than available.
 * HTTP: 400 Bad Request
 */
public class InsufficientStockException extends DomainException {
    public InsufficientStockException(long available, long requested) {
        super(String.format("Insufficient stock. Available: %d, Requested: %d", available, requested));
    }
}
