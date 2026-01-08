package com.fivetpromart.domain.exception;

/**
 * Thrown when product data validation fails.
 * HTTP: 400 Bad Request
 */
public class InvalidProductDataException extends DomainException {
    public InvalidProductDataException(String message) {
        super(message);
    }
}
