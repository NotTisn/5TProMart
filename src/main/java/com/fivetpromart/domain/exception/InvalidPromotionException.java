package com.fivetpromart.domain.exception;

/**
 * Exception thrown when promotion validation fails
 */
public class InvalidPromotionException extends RuntimeException {
    
    public InvalidPromotionException(String message) {
        super(message);
    }
    
    public InvalidPromotionException(String message, Throwable cause) {
        super(message, cause);
    }
}
