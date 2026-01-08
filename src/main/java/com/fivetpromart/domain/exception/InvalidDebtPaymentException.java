package com.fivetpromart.domain.exception;

/**
 * Thrown when trying to pay more debt than current debt amount.
 * HTTP: 400 Bad Request
 */
public class InvalidDebtPaymentException extends DomainException {
    public InvalidDebtPaymentException(String message) {
        super(message);
    }
}
