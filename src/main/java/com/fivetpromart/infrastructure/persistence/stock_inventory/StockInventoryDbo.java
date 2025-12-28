package com.fivetpromart.infrastructure.persistence.stock_inventory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "stock_inventories")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StockInventoryDbo {
    @Id
    @Column(name = "lot_id")
    String lotId;

    @Column(name = "product_id")
    String productId;

    @Column(name = "manufacture_date")
    LocalDate manufactureDate;

    @Column(name = "expiration_date")
    LocalDate expirationDate;

    @Column(name = "stock_quantity")
    Long stockQuantity;

    @Column(name = "import_price")
    BigDecimal importPrice;

    @Column(name = "status")
    String status;
}
