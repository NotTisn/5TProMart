package com.fivetpromart.domain.exception;

/**
 * Thrown when customer is not found.
 * HTTP: 404 Not Found
 */
public class CustomerNotFoundException extends DomainException {
    public CustomerNotFoundException(String customerId) {
        super(String.format("Customer with id '%s' not found", customerId));
    }
}
