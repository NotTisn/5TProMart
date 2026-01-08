package com.fivetpromart.domain.exception;

/**
 * Thrown when stock inventory data validation fails.
 * HTTP: 400 Bad Request
 */
public class InvalidStockDataException extends DomainException {
    public InvalidStockDataException(String message) {
        super(message);
    }
}
