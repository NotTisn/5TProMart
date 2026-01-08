package com.fivetpromart.domain.exception;

/**
 * Thrown when category is not found.
 * HTTP: 404 Not Found
 */
public class CategoryNotFoundException extends DomainException {
    public CategoryNotFoundException(String categoryId) {
        super(String.format("Category with id '%s' not found", categoryId));
    }
}
