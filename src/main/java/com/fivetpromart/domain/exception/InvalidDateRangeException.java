package com.fivetpromart.domain.exception;

/**
 * Thrown when expiration date is before manufacture date.
 * HTTP: 400 Bad Request
 */
public class InvalidDateRangeException extends DomainException {
    public InvalidDateRangeException(String message) {
        super(message);
    }

    public InvalidDateRangeException() {
        super("Expiration date must be after manufacture date");
    }
}
