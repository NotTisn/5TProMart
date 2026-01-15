package com.fivetpromart.infrastructure.persistence.stock_inventory.spec;

import com.fivetpromart.application.dto.query.StockInventorySearchQuery;
import com.fivetpromart.infrastructure.persistence.stock_inventory.StockInventoryDbo;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class StockInventorySpecification {

    /**
     * Build dynamic query specification from StockInventorySearchQuery
     */
    public static Specification<StockInventoryDbo> getSpecification(StockInventorySearchQuery query) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Search in lotId (contains)
            if (query.getSearch() != null && !query.getSearch().isBlank()) {
                String searchPattern = "%" + query.getSearch().toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("lotId")),
                        searchPattern
                ));
            }

            // Filter by productId
            if (query.getProductId() != null && !query.getProductId().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("productId"), query.getProductId()));
            }

            // Filter by status
            if (query.getStatus() != null && !query.getStatus().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), query.getStatus()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
