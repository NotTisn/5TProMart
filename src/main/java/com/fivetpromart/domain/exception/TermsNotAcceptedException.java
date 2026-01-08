package com.fivetpromart.domain.exception;

/**
 * Thrown when terms are not accepted.
 * HTTP: 400 Bad Request
 */
public class TermsNotAcceptedException extends DomainException {
    public TermsNotAcceptedException() {
        super("Terms and conditions must be accepted");
    }
}
