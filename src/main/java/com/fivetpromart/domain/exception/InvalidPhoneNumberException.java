package com.fivetpromart.domain.exception;

/**
 * Thrown when phone number is invalid.
 * HTTP: 400 Bad Request
 */
public class InvalidPhoneNumberException extends DomainException {
    public InvalidPhoneNumberException(String message) {
        super(message);
    }

    public InvalidPhoneNumberException() {
        super("Phone number must be 10 digits");
    }
}
