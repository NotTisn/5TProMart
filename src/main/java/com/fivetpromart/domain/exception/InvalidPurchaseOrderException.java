package com.fivetpromart.domain.exception;

/**
 * Thrown when purchase order validation fails.
 * HTTP: 400 Bad Request
 */
public class InvalidPurchaseOrderException extends DomainException {
    public InvalidPurchaseOrderException(String message) {
        super(message);
    }
}
