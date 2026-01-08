package com.fivetpromart.domain.exception;

/**
 * Generic exception for resource already exists.
 * HTTP: 409 Conflict
 */
public class ResourceAlreadyExistsException extends DomainException {
    public ResourceAlreadyExistsException(String resourceType, String identifier) {
        super(String.format("%s '%s' already exists", resourceType, identifier));
    }

    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
