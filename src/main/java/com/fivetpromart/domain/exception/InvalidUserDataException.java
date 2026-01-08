package com.fivetpromart.domain.exception;

/**
 * Thrown when user data validation fails.
 * HTTP: 400 Bad Request
 */
public class InvalidUserDataException extends DomainException {
    public InvalidUserDataException(String message) {
        super(message);
    }
}
