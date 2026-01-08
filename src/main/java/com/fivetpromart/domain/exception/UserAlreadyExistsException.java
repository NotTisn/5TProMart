package com.fivetpromart.domain.exception;

/**
 * Thrown when user already exists (duplicate username/email).
 * HTTP: 409 Conflict
 */
public class UserAlreadyExistsException extends DomainException {
    public UserAlreadyExistsException(String identifier) {
        super(String.format("User with '%s' already exists", identifier));
    }
}
