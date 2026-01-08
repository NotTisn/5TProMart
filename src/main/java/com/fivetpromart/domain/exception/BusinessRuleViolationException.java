package com.fivetpromart.domain.exception;

/**
 * Thrown when a business rule is violated.
 * HTTP: 400 Bad Request
 */
public class BusinessRuleViolationException extends DomainException {
    public BusinessRuleViolationException(String message) {
        super(message);
    }
}
