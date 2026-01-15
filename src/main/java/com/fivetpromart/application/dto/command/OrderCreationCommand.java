package com.fivetpromart.application.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreationCommand {
    private String staffId;
    private String customerId;  // Nullable for walk-in customers
    private String paymentMethod;  // CASH or BANK_TRANSFER
    private BigDecimal amountGiven;
    private List<OrderItemCommand> items;
    
    // NEW: Optional discount parameters (Polymorphism support)
    private DiscountCommand discount;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiscountCommand {
        private String type;  // PERCENTAGE, FIXED_AMOUNT, LOYALTY_POINTS, NONE
        private BigDecimal percentage;  // For PERCENTAGE type
        private BigDecimal maxAmount;   // Optional cap for PERCENTAGE
        private BigDecimal amount;      // For FIXED_AMOUNT type
        private Long pointsToUse;       // For LOYALTY_POINTS type
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemCommand {
        private String lotId;
        private Long quantity;
    }
}
