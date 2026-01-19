package com.fivetpromart.infrastructure.persistence.purchase_order;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "purchase_order_items")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseOrderItemDbo {

    @Id
    @Column(name = "item_id")
    String itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id")
    PurchaseOrderDbo purchaseOrder;

    @Column(name = "product_id")
    String productId;

    @Column(name = "product_name")
    String productName;

    @Column(name = "import_price", precision = 15, scale = 2)
    BigDecimal importPrice;

    @Column(name = "quantity_ordered")
    Long quantityOrdered;

    @Column(name = "quantity_received")
    Long quantityReceived;

    @Column(name = "sub_total", precision = 15, scale = 2)
    BigDecimal subTotal;
}
