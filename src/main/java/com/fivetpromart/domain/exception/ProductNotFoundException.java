package com.fivetpromart.domain.exception;

/**
 * Thrown when product is not found.
 * HTTP: 404 Not Found
 */
public class ProductNotFoundException extends DomainException {
    public ProductNotFoundException(String productId) {
        super(String.format("Product with id '%s' not found", productId));
    }
}
