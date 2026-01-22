package com.fivetpromart.domain.enums;

/**
 * Status of a stock inventory batch/lot.
 * 
 * Values:
 * - AVAILABLE: Lot has stock and is not expired
 * - OUT_OF_STOCK: Lot quantity is 0 or less
 * - EXPIRED: Lot expiration date has passed
 * - DISPOSED: Lot has been disposed (damaged, recalled, etc.)
 */
public enum BatchStatus {
    AVAILABLE("AVAILABLE"),
    OUT_OF_STOCK("OUT_OF_STOCK"),
    EXPIRED("EXPIRED"),
    DISPOSED("DISPOSED");

    private final String value;

    BatchStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * Parse from string (case-insensitive).
     * Maps legacy lowercase values to proper enum.
     */
    public static BatchStatus fromString(String text) {
        if (text == null || text.isBlank()) {
            return AVAILABLE; // Default for empty/null
        }
        
        // Normalize: handle legacy lowercase values
        String normalized = text.trim().toUpperCase().replace("-", "_");
        
        // Map legacy frontend values
        switch (normalized) {
            case "ACTIVE":
                return AVAILABLE;
            case "SOLD_OUT":
            case "SOLDOUT":
                return OUT_OF_STOCK;
            default:
                // Try direct match
                for (BatchStatus status : BatchStatus.values()) {
                    if (status.value.equalsIgnoreCase(text)) {
                        return status;
                    }
                }
                // Default fallback
                return AVAILABLE;
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
