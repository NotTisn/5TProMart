package com.fivetpromart.domain.exception;

/**
 * Thrown when role is not found.
 * HTTP: 404 Not Found
 */
public class RoleNotFoundException extends DomainException {
    public RoleNotFoundException(String roleName) {
        super(String.format("Role '%s' not found", roleName));
    }
}
