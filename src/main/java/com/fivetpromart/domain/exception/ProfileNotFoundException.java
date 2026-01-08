package com.fivetpromart.domain.exception;

/**
 * Thrown when profile is not found.
 * HTTP: 404 Not Found
 */
public class ProfileNotFoundException extends DomainException {
    public ProfileNotFoundException(String userId) {
        super(String.format("Profile for user '%s' not found", userId));
    }
}
