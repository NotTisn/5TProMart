package com.fivetpromart.domain.exception;

/**
 * Thrown when customer data validation fails.
 * HTTP: 400 Bad Request
 */
public class InvalidCustomerDataException extends DomainException {
    public InvalidCustomerDataException(String message) {
        super(message);
    }
}
