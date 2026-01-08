package com.fivetpromart.domain.exception;

/**
 * Thrown when profile already exists.
 * HTTP: 409 Conflict
 */
public class ProfileAlreadyExistsException extends DomainException {
    public ProfileAlreadyExistsException(String userId) {
        super(String.format("Profile for user '%s' already exists", userId));
    }
}
