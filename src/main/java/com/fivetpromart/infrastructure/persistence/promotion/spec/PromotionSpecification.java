package com.fivetpromart.infrastructure.persistence.promotion.spec;

import com.fivetpromart.application.dto.query.PromotionSearchQuery;
import com.fivetpromart.infrastructure.persistence.promotion.PromotionDbo;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PromotionSpecification {

    public static Specification<PromotionDbo> getSpec(PromotionSearchQuery query) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // SEARCH: promotionId, promotionName, productName
            if (query.getSearch() != null && !query.getSearch().isBlank()) {
                String searchPattern = "%" + query.getSearch().toLowerCase() + "%";

                Predicate promotionIdPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("promotionId")),
                        searchPattern
                );

                Predicate promotionNamePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("promotionName")),
                        searchPattern
                );

                predicates.add(criteriaBuilder.or(promotionIdPredicate, promotionNamePredicate));
            }

            // FILTER: type
            if (query.getType() != null && !query.getType().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("promotionType"), query.getType()));
            }

            // FILTER: status
            if (query.getStatus() != null && !query.getStatus().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), query.getStatus()));
            }

            // FILTER: startDate
            if (query.getStartDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), query.getStartDate()));
            }

            // FILTER: endDate
            if (query.getEndDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), query.getEndDate()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
