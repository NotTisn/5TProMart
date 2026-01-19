package com.fivetpromart.infrastructure.persistence.product.spec;

import com.fivetpromart.application.dto.query.ProductSearchQuery;
import com.fivetpromart.infrastructure.persistence.product.ProductDbo;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

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

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}