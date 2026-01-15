package com.fivetpromart.domain.exception;

/**
 * Thrown when stock lot has expired.
 * HTTP: 400 Bad Request
 */
public class ExpiredLotException extends DomainException {
    public ExpiredLotException(String lotId) {
        super(String.format("Stock lot with id '%s' has expired", lotId));
    }
}
