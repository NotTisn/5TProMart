package com.fivetpromart.domain.exception;

/**
 * Thrown when input data is invalid (format, range, etc.).
 * HTTP: 400 Bad Request
 */
public class InvalidInputException extends DomainException {
    public InvalidInputException(String message) {
        super(message);
    }
}
