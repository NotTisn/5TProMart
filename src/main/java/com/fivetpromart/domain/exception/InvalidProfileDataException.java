package com.fivetpromart.domain.exception;

/**
 * Thrown when profile data validation fails.
 * HTTP: 400 Bad Request
 */
public class InvalidProfileDataException extends DomainException {
    public InvalidProfileDataException(String message) {
        super(message);
    }
}
