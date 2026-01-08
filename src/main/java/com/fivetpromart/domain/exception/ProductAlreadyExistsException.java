package com.fivetpromart.domain.exception;

/**
 * Thrown when product already exists.
 * HTTP: 409 Conflict
 */
public class ProductAlreadyExistsException extends DomainException {
    public ProductAlreadyExistsException(String productName) {
        super(String.format("Product '%s' already exists", productName));
    }
}
