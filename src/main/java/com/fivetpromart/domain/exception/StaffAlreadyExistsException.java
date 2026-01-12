package com.fivetpromart.domain.exception;

public class StaffAlreadyExistsException extends RuntimeException {
    public StaffAlreadyExistsException(String email) {
        super(String.format("Staff with email '%s' already exists", email));
    }
}
