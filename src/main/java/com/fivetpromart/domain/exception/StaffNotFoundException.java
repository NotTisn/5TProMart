package com.fivetpromart.domain.exception;

public class StaffNotFoundException extends RuntimeException {
    public StaffNotFoundException(String staffId) {
        super("Staff not found with ID: " + staffId);
    }
}
