package com.fivetpromart.infrastructure.persistence.product.spec;

import com.fivetpromart.application.dto.query.ProductSearchQuery;
import com.fivetpromart.infrastructure.persistence.product.ProductDbo;
import com.fivetpromart.infrastructure.persistence.stock_inventory.StockInventoryDbo;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    /** Low stock threshold - same as InventoryProperties default */
    private static final Long LOW_STOCK_THRESHOLD = 10L;

    /** Default expiry warning days - same as InventoryProperties default */
    private static final int DEFAULT_EXPIRY_WARNING_DAYS = 7;

    /**
     * Build specification with configurable expiry threshold.
     * 
     * @param query The search query parameters
     * @param expiryWarningDays Number of days to consider as "expiring soon"
     * @return JPA Specification for ProductDbo
     */
    public static Specification<ProductDbo> getSpec(ProductSearchQuery query, int expiryWarningDays) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 0. Filter by isActive status (default: only active products)
            if (query.getIsActive() != null) {
                predicates.add(criteriaBuilder.equal(root.get("isActive"), query.getIsActive()));
            } else {
                Boolean includeDeleted = query.getIncludeDeleted();
                if (includeDeleted == null || !includeDeleted) {
                    predicates.add(criteriaBuilder.equal(root.get("isActive"), true));
                }
            }

            // 1. Filter by ID (Exact match)
            if (query.getProductId() != null && !query.getProductId().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("productId"), query.getProductId()));
            }

            // 2. Filter by Category (Exact match)
            if (query.getCategoryId() != null && !query.getCategoryId().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("categoryId"), query.getCategoryId()));
            }

            // 3. Filter by Name (Contains / Like)
            if (query.getProductName() != null && !query.getProductName().isBlank()) {
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
                    case "expiring-soon":
                        // Products with at least one lot expiring within threshold days
                        LocalDate today = LocalDate.now();
                        LocalDate warningDate = today.plusDays(expiryWarningDays);
                        
                        Subquery<String> expiringSoonSubquery = criteriaQuery.subquery(String.class);
                        Root<StockInventoryDbo> stockRoot = expiringSoonSubquery.from(StockInventoryDbo.class);
                        expiringSoonSubquery.select(stockRoot.get("productId"))
                                .where(criteriaBuilder.and(
                                        criteriaBuilder.isNotNull(stockRoot.get("expirationDate")),
                                        criteriaBuilder.greaterThanOrEqualTo(stockRoot.get("expirationDate"), today),
                                        criteriaBuilder.lessThanOrEqualTo(stockRoot.get("expirationDate"), warningDate),
                                        criteriaBuilder.greaterThan(stockRoot.get("stockQuantity"), 0L)
                                ));
                        predicates.add(root.get("productId").in(expiringSoonSubquery));
                        break;
                    case "expired":
                        // Products with at least one expired lot that still has stock
                        LocalDate now = LocalDate.now();
                        
                        Subquery<String> expiredSubquery = criteriaQuery.subquery(String.class);
                        Root<StockInventoryDbo> expiredStockRoot = expiredSubquery.from(StockInventoryDbo.class);
                        expiredSubquery.select(expiredStockRoot.get("productId"))
                                .where(criteriaBuilder.and(
                                        criteriaBuilder.isNotNull(expiredStockRoot.get("expirationDate")),
                                        criteriaBuilder.lessThan(expiredStockRoot.get("expirationDate"), now),
                                        criteriaBuilder.greaterThan(expiredStockRoot.get("stockQuantity"), 0L)
                                ));
                        predicates.add(root.get("productId").in(expiredSubquery));
                        break;
                    default:
                        // Unknown stock level, ignore
                        break;
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Backward-compatible method using default expiry threshold.
     */
    public static Specification<ProductDbo> getSpec(ProductSearchQuery query) {
        return getSpec(query, DEFAULT_EXPIRY_WARNING_DAYS);
    }
}