package com.fivetpromart.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
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
public class OrderRequest {

//    @NotBlank(message = "Staff ID is required")
//    private String staffId;

    private String customerId;  // Optional - null for walk-in customers

    @NotBlank(message = "Payment method is required")
    @Pattern(regexp = "^(CASH|BANK_TRANSFER)$", message = "Payment method must be CASH or BANK_TRANSFER")
    private String paymentMethod;

    @NotNull(message = "Amount given is required")
    @Positive(message = "Amount given must be positive")
    private BigDecimal amountGiven;

    @NotEmpty(message = "Order must have at least one item")
    @Valid
    private List<OrderItemRequest> items;
    
    // NEW: Optional discount (Polymorphism support)
    @Valid
    private DiscountRequest discount;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiscountRequest {
        @NotBlank(message = "Discount type is required")
        private String type;  // PERCENTAGE, FIXED_AMOUNT, LOYALTY_POINTS, NONE
        
        @Positive(message = "Percentage must be positive")
        private BigDecimal percentage;
        
        @Positive(message = "Max amount must be positive")
        private BigDecimal maxAmount;
        
        @Positive(message = "Amount must be positive")
        private BigDecimal amount;
        
        @Positive(message = "Points to use must be positive")
        private Long pointsToUse;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemRequest {
        @NotBlank(message = "Lot ID is required")
        private String lotId;

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be positive")
        private Long quantity;
    }
}
