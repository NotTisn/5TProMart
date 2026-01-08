package com.fivetpromart.domain.exception;

/**
 * Generic exception for resource not found.
 * HTTP: 404 Not Found
 */
public class ResourceNotFoundException extends DomainException {
    public ResourceNotFoundException(String resourceType, String resourceId) {
        super(String.format("%s with id '%s' not found", resourceType, resourceId));
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
