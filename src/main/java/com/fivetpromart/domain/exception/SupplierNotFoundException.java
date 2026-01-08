package com.fivetpromart.domain.exception;

/**
 * Thrown when supplier is not found.
 * HTTP: 404 Not Found
 */
public class SupplierNotFoundException extends DomainException {
    public SupplierNotFoundException(String supplierId) {
        super(String.format("Supplier with id '%s' not found", supplierId));
    }
}
