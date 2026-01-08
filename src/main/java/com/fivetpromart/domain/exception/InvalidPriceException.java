package com.fivetpromart.domain.exception;

/**
 * Thrown when product price is invalid (negative or zero).
 * HTTP: 400 Bad Request
 */
public class InvalidPriceException extends DomainException {
    public InvalidPriceException(String message) {
        super(message);
    }

    public InvalidPriceException() {
        super("Price must be positive");
    }
}
