package com.fivetpromart.domain.exception;

/**
 * Thrown when category already exists.
 * HTTP: 409 Conflict
 */
public class CategoryAlreadyExistsException extends DomainException {
    public CategoryAlreadyExistsException(String categoryName) {
        super(String.format("Category '%s' already exists", categoryName));
    }
}
