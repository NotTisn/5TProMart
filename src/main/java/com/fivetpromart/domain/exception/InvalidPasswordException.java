package com.fivetpromart.domain.exception;

/**
 * Thrown when password is invalid (too short, weak, etc.).
 * HTTP: 400 Bad Request
 */
public class InvalidPasswordException extends DomainException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
