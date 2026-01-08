package com.fivetpromart.domain.exception;

/**
 * Thrown when required field is empty/null/blank.
 * HTTP: 400 Bad Request
 */
public class EmptyFieldException extends DomainException {
    public EmptyFieldException(String fieldName) {
        super(String.format("%s cannot be empty", fieldName));
    }
}
