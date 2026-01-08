package com.fivetpromart.domain.exception;

/**
 * Thrown when user is not found.
 * HTTP: 404 Not Found
 */
public class UserNotFoundException extends DomainException {
    public UserNotFoundException(String userId) {
        super(String.format("User with id '%s' not found", userId));
    }

    public UserNotFoundException() {
        super("User not found");
    }
}
