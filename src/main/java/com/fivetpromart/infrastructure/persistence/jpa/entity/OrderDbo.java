package com.fivetpromart.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDbo {

    @Id
    @Column(name = "order_id", length = 50)
    private String orderId;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "staff_id", length = 50, nullable = false)
    private String staffId;

    @Column(name = "customer_id", length = 50)
    private String customerId;  // Nullable for walk-in customers

    @Column(name = "payment_method", length = 50, nullable = false)
    private String paymentMethod;  // CASH, BANK_TRANSFER

    @Column(name = "status", length = 50, nullable = false)
    private String status;  // PAID, UNPAID, CANCELLED

    @Column(name = "sub_total", precision = 15, scale = 2, nullable = false)
    private BigDecimal subTotal;

    @Column(name = "discount_amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal discountAmount;

    @Column(name = "total_amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "amount_given", precision = 15, scale = 2, nullable = false)
    private BigDecimal amountGiven;

    @Column(name = "change_returned", precision = 15, scale = 2, nullable = false)
    private BigDecimal changeReturned;
    
    // Cash rounding fields (Vietnam retail standard)
    @Column(name = "original_amount", precision = 15, scale = 2)
    private BigDecimal originalAmount;
    
    @Column(name = "rounding_adjustment", precision = 15, scale = 2)
    private BigDecimal roundingAdjustment;

    @Column(name = "points_earned", nullable = false)
    private Long pointsEarned;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<OrderItemDbo> items = new ArrayList<>();

    public void addItem(OrderItemDbo item) {
        items.add(item);
        item.setOrder(this);
    }

    public void removeItem(OrderItemDbo item) {
        items.remove(item);
        item.setOrder(null);
    }
}
