package com.fivetpromart.domain.exception;

/**
 * Thrown when an operation is not allowed in current state.
 * HTTP: 400 Bad Request
 */
public class InvalidOperationException extends DomainException {
    public InvalidOperationException(String message) {
        super(message);
    }
}
