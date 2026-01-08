package com.fivetpromart.domain.exception;

/**
 * Thrown when supplier already exists.
 * HTTP: 409 Conflict
 */
public class SupplierAlreadyExistsException extends DomainException {
    public SupplierAlreadyExistsException(String supplierName) {
        super(String.format("Supplier '%s' already exists", supplierName));
    }
}
