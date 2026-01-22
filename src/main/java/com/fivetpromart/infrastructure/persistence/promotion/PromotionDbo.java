package com.fivetpromart.infrastructure.persistence.promotion;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "promotions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromotionDbo {
    @Id
    @Column(name = "promotion_id", length = 50)
    String promotionId;

    @Column(name = "promotion_name", nullable = false)
    String promotionName;

    @Column(name = "promotion_description", columnDefinition = "TEXT")
    String promotionDescription;

//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//    @JoinColumn(name = "promotion_id")
//    @Builder.Default
//    List<PromotionProductDbo> products = new ArrayList<>();

    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    List<PromotionProductDbo> products = new ArrayList<>();

    @Column(name = "promotion_type", nullable = false)
    String promotionType;

    @Column(name = "discount_percent")
    Integer discountPercent;

    @Column(name = "buy_quantity")
    Integer buyQuantity;

    @Column(name = "get_quantity")
    Integer getQuantity;

    @Column(name = "start_date", nullable = false)
    LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    LocalDate endDate;

    @Column(name = "status", nullable = false)
    String status;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;
}
