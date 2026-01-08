package com.fivetpromart.domain.exception;

/**
 * Thrown when numeric value is negative when it should be positive.
 * HTTP: 400 Bad Request
 */
public class NegativeValueException extends DomainException {
    public NegativeValueException(String fieldName) {
        super(String.format("%s cannot be negative", fieldName));
    }
}
