package com.fivetpromart.domain.exception;

/**
 * Thrown when email is invalid.
 * HTTP: 400 Bad Request
 */
public class InvalidEmailException extends DomainException {
    public InvalidEmailException(String message) {
        super(message);
    }

    public InvalidEmailException() {
        super("Invalid email format");
    }
}
