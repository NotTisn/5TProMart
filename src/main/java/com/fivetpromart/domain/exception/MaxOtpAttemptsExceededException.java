package com.fivetpromart.domain.exception;

public class MaxOtpAttemptsExceededException extends RuntimeException {
    public MaxOtpAttemptsExceededException(String message) {
        super(message);
    }
}
