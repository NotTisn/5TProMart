package com.fivetpromart.domain.exception;

/**
 * Thrown when category data validation fails.
 * HTTP: 400 Bad Request
 */
public class InvalidCategoryDataException extends DomainException {
    public InvalidCategoryDataException(String message) {
        super(message);
    }
}
