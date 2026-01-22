package com.fivetpromart.infrastructure.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for inventory management.
 * 
 * Centralizes all inventory-related thresholds and settings
 * that can be configured via application.yml or environment variables.
 * 
 * Usage:
 *   inventory:
 *     low-stock-threshold: 10
 *     expiry-warning-days: 7
 */
@Component
@Getter
public class InventoryProperties {

    /**
     * Products with stock quantity below this threshold are considered "low stock".
     * Default: 10 units
     */
    @Value("${inventory.low-stock-threshold:10}")
    private long lowStockThreshold;

    /**
     * Number of days to look ahead for expiring inventory.
     * Products expiring within this window are flagged as "expiring soon".
     * 
     * Vietnam retail standard: 7 days
     * 
     * Default: 7 days (aligned with frontend for consistent UX)
     */
    @Value("${inventory.expiry-warning-days:7}")
    private int expiryWarningDays;
}
