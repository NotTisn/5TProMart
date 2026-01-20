package com.fivetpromart.infrastructure.persistence.purchase_order.spec;

import com.fivetpromart.application.dto.query.PurchaseOrderSearchQuery;
import com.fivetpromart.infrastructure.persistence.purchase_order.PurchaseOrderDbo;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderSpecification {

    public static Specification<PurchaseOrderDbo> getSpecification(PurchaseOrderSearchQuery query) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Search in poCode or supplierName
            if (query.getSearch() != null && !query.getSearch().isBlank()) {
                String searchPattern = "%" + query.getSearch().toLowerCase() + "%";
                Predicate poCodePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("poCode")),
                        searchPattern
                );
                Predicate supplierNamePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("supplier").get("supplierName")),
                        searchPattern
                );
                predicates.add(criteriaBuilder.or(poCodePredicate, supplierNamePredicate));
            }

            // Filter by supplierId
            if (query.getSupplierId() != null && !query.getSupplierId().isBlank()) {
                predicates.add(criteriaBuilder.equal(
                        root.get("supplier").get("supplierId"),
                        query.getSupplierId()
                ));
            }

            // Filter by status
            if (query.getStatus() != null && !query.getStatus().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), query.getStatus()));
            }

            // Filter by date range
            if (query.getStartDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("purchaseDate"),
                        query.getStartDate()
                ));
            }

            if (query.getEndDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("purchaseDate"),
                        query.getEndDate()
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
