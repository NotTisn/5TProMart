package com.fivetpromart.domain.exception;

/**
 * Thrown when stock lot is not found.
 * HTTP: 404 Not Found
 */
public class StockLotNotFoundException extends DomainException {
    public StockLotNotFoundException(String lotId) {
        super(String.format("Stock lot with id '%s' not found", lotId));
    }
}
