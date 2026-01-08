package com.fivetpromart.domain.exception;

/**
 * Thrown when username is invalid (too short, invalid chars, etc.).
 * HTTP: 400 Bad Request
 */
public class InvalidUsernameException extends DomainException {
    public InvalidUsernameException(String message) {
        super(message);
    }
}
