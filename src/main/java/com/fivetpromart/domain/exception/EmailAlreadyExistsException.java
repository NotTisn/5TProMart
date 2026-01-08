package com.fivetpromart.domain.exception;

/**
 * Thrown when email already exists.
 * HTTP: 409 Conflict
 */
public class EmailAlreadyExistsException extends DomainException {
    public EmailAlreadyExistsException(String email) {
        super(String.format("Email '%s' already exists", email));
    }
}
