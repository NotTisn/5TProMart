package com.fivetpromart.domain.exception;

/**
 * Thrown when supplier data validation fails.
 * HTTP: 400 Bad Request
 */
public class InvalidSupplierDataException extends DomainException {
    public InvalidSupplierDataException(String message) {
        super(message);
    }
}
