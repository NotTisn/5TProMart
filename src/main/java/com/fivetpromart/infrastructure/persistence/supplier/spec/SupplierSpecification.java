package com.fivetpromart.infrastructure.persistence.supplier.spec;

import com.fivetpromart.application.dto.query.ProductSearchQuery;
import com.fivetpromart.application.dto.query.SupplierSearchQuery;
import com.fivetpromart.infrastructure.persistence.product.ProductDbo;
import com.fivetpromart.infrastructure.persistence.supplier.SupplierDbo;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class SupplierSpecification {

    public static Specification<SupplierDbo> getSpec(SupplierSearchQuery query) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Lọc theo ID (Exact match)
            if (query.getSupplierId() != null && !query.getSupplierId().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("supplierId"), query.getSupplierId()));
            }

            // 2. Lọc theo Category (Exact match)
            if (query.getSupplierType() != null && !query.getSupplierType().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("supplierType"), query.getSupplierType()));
            }

            // 3. Lọc theo Tên (Contains / Like)
            if (query.getSupplierName() != null && !query.getSupplierName().isBlank()) {
                // lower(productName) like %value%
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("productName")),
                        "%" + query.getSupplierName().toLowerCase() + "%"
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}