package com.fivetpromart.infrastructure.persistence.product.spec;

import com.fivetpromart.application.dto.query.ProductSearchQuery;
import com.fivetpromart.infrastructure.persistence.product.ProductDbo;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    /** Low stock threshold - same as ProductUseCase */
    private static final Long LOW_STOCK_THRESHOLD = 10L;

    public static Specification<ProductDbo> getSpec(ProductSearchQuery query) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 0. ALWAYS exclude soft-deleted products (NEW)
            predicates.add(criteriaBuilder.isNull(root.get("deletedAt")));

            // 1. Lọc theo ID (Exact match)
            if (query.getProductId() != null && !query.getProductId().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("productId"), query.getProductId()));
            }

            // 2. Lọc theo Category (Exact match)
            if (query.getCategoryId() != null && !query.getCategoryId().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("categoryId"), query.getCategoryId()));
            }

            // 3. Lọc theo Tên (Contains / Like)
            if (query.getProductName() != null && !query.getProductName().isBlank()) {
                // lower(productName) like %value%
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("productName")),
                        "%" + query.getProductName().toLowerCase() + "%"
                ));
            }

            // 4. Stock Level Filter (for stats drill-down)
            if (query.getStockLevel() != null && !query.getStockLevel().isBlank()) {
                switch (query.getStockLevel().toLowerCase()) {
                    case "low":
                        // Products with stock > 0 and < threshold
                        predicates.add(criteriaBuilder.greaterThan(root.get("totalStockQuantity"), 0L));
                        predicates.add(criteriaBuilder.lessThan(root.get("totalStockQuantity"), LOW_STOCK_THRESHOLD));
                        break;
                    case "out":
                        // Products with zero stock
                        predicates.add(criteriaBuilder.equal(root.get("totalStockQuantity"), 0L));
                        break;
                    // Note: "expiring-soon" and "expired" require batch-level data
                    // These would need a subquery or join to StockInventory
                    // For MVP, we filter at product level based on totalStockQuantity
                    default:
                        // Unknown stock level, ignore
                        break;
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}