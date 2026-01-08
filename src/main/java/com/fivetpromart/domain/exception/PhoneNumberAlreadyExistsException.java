package com.fivetpromart.domain.exception;

/**
 * Thrown when phone number already exists.
 * HTTP: 409 Conflict
 */
public class PhoneNumberAlreadyExistsException extends DomainException {
    public PhoneNumberAlreadyExistsException(String phoneNumber) {
        super(String.format("Phone number '%s' already exists", phoneNumber));
    }
}
