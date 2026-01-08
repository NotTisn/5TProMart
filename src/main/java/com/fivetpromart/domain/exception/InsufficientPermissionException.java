package com.fivetpromart.domain.exception;

/**
 * Thrown when user doesn't have required permission.
 * HTTP: 403 Forbidden
 */
public class InsufficientPermissionException extends DomainException {
    public InsufficientPermissionException(String message) {
        super(message);
    }

    public InsufficientPermissionException() {
        super("You do not have permission to perform this action");
    }
}
