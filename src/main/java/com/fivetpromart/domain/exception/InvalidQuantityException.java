package com.fivetpromart.domain.exception;

/**
 * Thrown when stock quantity is invalid (negative or zero).
 * HTTP: 400 Bad Request
 */
public class InvalidQuantityException extends DomainException {
    public InvalidQuantityException(String message) {
        super(message);
    }

    public InvalidQuantityException() {
        super("Quantity must be positive");
    }
}
