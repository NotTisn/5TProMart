package com.fivetpromart.domain.model;

import com.fivetpromart.domain.exception.EmptyFieldException;
import com.fivetpromart.domain.exception.InvalidOrderException;
import com.fivetpromart.domain.exception.NegativeValueException;
import com.fivetpromart.domain.model.state.OrderState;
import com.fivetpromart.domain.model.state.OrderStateFactory;
import com.fivetpromart.domain.model.state.PaidOrderState;
import com.fivetpromart.domain.model.strategy.discount.DiscountStrategy;
import com.fivetpromart.domain.model.strategy.discount.NoDiscountStrategy;
import com.fivetpromart.domain.model.strategy.notification.NotificationStrategy;
import com.fivetpromart.domain.model.strategy.payment.PaymentResult;
import com.fivetpromart.domain.model.strategy.payment.PaymentStrategy;
import com.fivetpromart.domain.model.strategy.payment.PaymentStrategyFactory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Order {
    private String orderId;
    private LocalDateTime orderDate;
    private String staffId;
    private String customerId;  // Nullable - "Khách lẻ" if null
    
    // Polymorphic strategies
    private PaymentStrategy paymentStrategy;
    private DiscountStrategy discountStrategy;
    private OrderState orderState;
    private NotificationStrategy notificationStrategy;
    
    // Backward compatibility fields (derived from strategies)
    private String paymentMethod;  // Derived from paymentStrategy.getPaymentMethod()
    private String status;  // Derived from orderState.getStateName()
    
    private BigDecimal subTotal;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private BigDecimal amountGiven;
    private BigDecimal changeReturned;
    private Long pointsEarned;
    private List<OrderItem> items;

    // =================================================================
    // 1. FACTORY: CREATE NEW ORDER (with Polymorphism)
    // =================================================================
    public static Order create(
            String staffId,
            String customerId,
            String paymentMethod,
            BigDecimal amountGiven,
            List<OrderItem> items
    ) {
        return create(staffId, customerId, paymentMethod, amountGiven, items, 
                     new NoDiscountStrategy(), null);
    }
    
    /**
     * Create order with discount strategy
     */
    public static Order create(
            String staffId,
            String customerId,
            String paymentMethod,
            BigDecimal amountGiven,
            List<OrderItem> items,
            DiscountStrategy discountStrategy,
            NotificationStrategy notificationStrategy
    ) {
        // Validation
//        if (staffId == null || staffId.isBlank()) {
//            throw new EmptyFieldException("Staff ID");
//        }
        if (items == null || items.isEmpty()) {
            throw new InvalidOrderException("Order must have at least one item");
        }
        if (amountGiven == null || amountGiven.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeValueException("Amount given");
        }

        Order order = new Order();
        order.orderId = UUID.randomUUID().toString();
        order.orderDate = LocalDateTime.now();
        order.staffId = staffId;
        order.customerId = customerId;  // Can be null for walk-in customers
        order.items = new ArrayList<>(items);
        
        // Set polymorphic strategies
        order.paymentStrategy = PaymentStrategyFactory.createStrategy(paymentMethod);
        order.discountStrategy = discountStrategy != null ? discountStrategy : new NoDiscountStrategy();
        order.orderState = new PaidOrderState();  // New orders are paid immediately
        order.notificationStrategy = notificationStrategy;
        
        // Backward compatibility
        order.paymentMethod = order.paymentStrategy.getPaymentMethod();
        order.status = order.orderState.getStateName();

        // Calculate totals with discount strategy
        order.calculateSubTotal();
        order.applyDiscountStrategy();

        // Process payment with payment strategy
        order.processPayment(amountGiven);

        // Calculate loyalty points (1% of total amount)
        order.pointsEarned = order.totalAmount.longValue() / 100;

        // Send notification if strategy provided
        if (order.notificationStrategy != null && order.notificationStrategy.isEnabled()) {
            try {
                order.notificationStrategy.notifyOrderCreated(order);
            } catch (Exception e) {
                log.warn("Failed to send order creation notification: {}", e.getMessage());
            }
        }

        return order;
    }

    // =================================================================
    // 2. FACTORY: RECONSTITUTE (Load from DB) - with Polymorphism
    // =================================================================
    public static Order reconstitute(
            String orderId,
            LocalDateTime orderDate,
            String staffId,
            String customerId,
            String paymentMethod,
            String status,
            BigDecimal subTotal,
            BigDecimal discountAmount,
            BigDecimal totalAmount,
            BigDecimal amountGiven,
            BigDecimal changeReturned,
            Long pointsEarned,
            List<OrderItem> items
    ) {
        Order order = new Order();
        order.orderId = orderId;
        order.orderDate = orderDate;
        order.staffId = staffId;
        order.customerId = customerId;
        order.subTotal = subTotal;
        order.discountAmount = discountAmount;
        order.totalAmount = totalAmount;
        order.amountGiven = amountGiven;
        order.changeReturned = changeReturned;
        order.pointsEarned = pointsEarned;
        order.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
        
        // Reconstitute polymorphic strategies from persisted data
        order.paymentStrategy = PaymentStrategyFactory.createStrategy(paymentMethod);
        order.orderState = OrderStateFactory.createState(status);
        order.discountStrategy = new NoDiscountStrategy();  // Default, can be enhanced
        order.notificationStrategy = null;  // Not persisted
        
        // Backward compatibility
        order.paymentMethod = paymentMethod;
        order.status = status;
        
        return order;
    }

    // =================================================================
    // 3. BUSINESS BEHAVIORS (with Polymorphism)
    // =================================================================

    /**
     * Calculate subtotal from items
     */
    private void calculateSubTotal() {
        this.subTotal = items.stream()
                .map(OrderItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Apply discount using discount strategy (Polymorphism)
     */
    private void applyDiscountStrategy() {
        this.discountAmount = discountStrategy.calculateDiscount(this.subTotal);
        this.totalAmount = this.subTotal.subtract(this.discountAmount);
        
        log.info("Applied discount: {} - {}", 
                discountStrategy.getDiscountType(), 
                discountStrategy.getDescription());
    }
    
    /**
     * Process payment using payment strategy (Polymorphism)
     */
    private void processPayment(BigDecimal amountGiven) {
        PaymentResult result = paymentStrategy.processPayment(this.totalAmount, amountGiven);
        
        if (!result.isSuccessful()) {
            throw new InvalidOrderException("Payment processing failed");
        }
        
        this.amountGiven = amountGiven;
        this.changeReturned = result.getChangeReturned();
        
        log.info("Payment processed: {} - Change: {}", 
                result.getPaymentMethod(), 
                result.getChangeReturned());
    }

    /**
     * Cancel order using state pattern (Polymorphism)
     */
    public void cancel() {
        log.info("Cancelling order {} in state {}", orderId, orderState.getStateName());
        
        orderState.cancel(this);
        this.status = orderState.getStateName();  // Update backward compatibility field
        
        // Send notification
        if (notificationStrategy != null && notificationStrategy.isEnabled()) {
            try {
                notificationStrategy.notifyOrderCancelled(this);
            } catch (Exception e) {
                log.warn("Failed to send order cancellation notification: {}", e.getMessage());
            }
        }
    }
    
    /**
     * Complete order using state pattern (Polymorphism)
     */
    public void complete() {
        log.info("Completing order {} in state {}", orderId, orderState.getStateName());
        
        orderState.complete(this);
        this.status = orderState.getStateName();  // Update backward compatibility field
        
        // Send notification
        if (notificationStrategy != null && notificationStrategy.isEnabled()) {
            try {
                notificationStrategy.notifyOrderCompleted(this);
            } catch (Exception e) {
                log.warn("Failed to send order completion notification: {}", e.getMessage());
            }
        }
    }

    /**
     * Apply discount to order using discount strategy (Public API)
     */
    public void applyDiscount(DiscountStrategy newDiscountStrategy) {
        if (newDiscountStrategy == null) {
            throw new InvalidOrderException("Discount strategy cannot be null");
        }
        
        log.info("Applying new discount strategy: {}", newDiscountStrategy.getDiscountType());
        
        this.discountStrategy = newDiscountStrategy;
        this.discountAmount = discountStrategy.calculateDiscount(this.subTotal);
        this.totalAmount = this.subTotal.subtract(this.discountAmount);
    }

    /**
     * Apply discount to order (backward compatibility)
     */
    public void applyDiscount(BigDecimal discountAmount) {
        if (discountAmount == null || discountAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeValueException("Discount amount");
        }
        if (discountAmount.compareTo(this.subTotal) > 0) {
            throw new InvalidOrderException("Discount amount cannot exceed subtotal");
        }
        this.discountAmount = discountAmount;
        this.totalAmount = this.subTotal.subtract(this.discountAmount);
    }
    
    /**
     * Set notification strategy
     */
    public void setNotificationStrategy(NotificationStrategy notificationStrategy) {
        this.notificationStrategy = notificationStrategy;
    }
    
    /**
     * Internal method to update state (called by State Pattern)
     */
    public void setState(OrderState newState) {
        this.orderState = newState;
        this.status = newState.getStateName();
    }

    // =================================================================
    // NESTED CLASS: ORDER ITEM
    // =================================================================
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class OrderItem {
        private String orderItemId;
        private String orderId;
        private String lotId;
        private String productId;
        private String productName;
        private Long quantity;
        private BigDecimal unitPrice;
        private BigDecimal subTotal;

        public static OrderItem create(
                String lotId,
                String productId,
                String productName,
                Long quantity,
                BigDecimal unitPrice
        ) {
            if (lotId == null || lotId.isBlank()) {
                throw new EmptyFieldException("Lot ID");
            }
            if (productId == null || productId.isBlank()) {
                throw new EmptyFieldException("Product ID");
            }
            if (productName == null || productName.isBlank()) {
                throw new EmptyFieldException("Product name");
            }
            if (quantity == null || quantity <= 0) {
                throw new InvalidOrderException("Quantity must be greater than 0");
            }
            if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) {
                throw new NegativeValueException("Unit price");
            }

            OrderItem item = new OrderItem();
            item.orderItemId = UUID.randomUUID().toString();
            item.lotId = lotId;
            item.productId = productId;
            item.productName = productName;
            item.quantity = quantity;
            item.unitPrice = unitPrice;
            item.subTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));

            return item;
        }

        public static OrderItem reconstitute(
                String orderItemId,
                String orderId,
                String lotId,
                String productId,
                String productName,
                Long quantity,
                BigDecimal unitPrice,
                BigDecimal subTotal
        ) {
            OrderItem item = new OrderItem();
            item.orderItemId = orderItemId;
            item.orderId = orderId;
            item.lotId = lotId;
            item.productId = productId;
            item.productName = productName;
            item.quantity = quantity;
            item.unitPrice = unitPrice;
            item.subTotal = subTotal;
            return item;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }
    }
}
