package com.fivetpromart.infrastructure.persistence.promotion;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "promotion_products")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromotionProductDbo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "promotion_id", nullable = false)
    String promotionId;

    @Column(name = "product_id", nullable = false)
    String productId;

    @Column(name = "product_name")
    String productName;
}
