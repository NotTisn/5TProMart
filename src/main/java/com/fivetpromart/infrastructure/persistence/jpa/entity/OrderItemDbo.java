package com.fivetpromart.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemDbo {

    @Id
    @Column(name = "order_item_id", length = 36)
    String orderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    OrderDbo order;

    @Column(name = "lot_id", nullable = false, length = 36)
    String lotId;

    @Column(name = "product_id", nullable = false, length = 36)
    String productId;

    @Column(name = "product_name", nullable = false)
    String productName;

    @Column(name = "quantity", nullable = false)
    Long quantity;

    @Column(name = "unit_price", nullable = false, precision = 15, scale = 2)
    BigDecimal unitPrice;

    @Column(name = "sub_total", nullable = false, precision = 15, scale = 2)
    BigDecimal subTotal;
}
