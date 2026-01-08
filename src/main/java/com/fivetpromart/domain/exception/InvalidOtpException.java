package com.fivetpromart.domain.exception;

public class InvalidOtpException extends DomainException {
    public InvalidOtpException(String message) {
        super(String.format("Invalid otp"));
    }
}
