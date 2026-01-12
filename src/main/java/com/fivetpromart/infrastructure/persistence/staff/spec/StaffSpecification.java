package com.fivetpromart.infrastructure.persistence.staff.spec;

import com.fivetpromart.application.dto.query.StaffSearchQuery;
import com.fivetpromart.infrastructure.persistence.staff.StaffDbo;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class StaffSpecification {

    public static Specification<StaffDbo> getSpec(StaffSearchQuery query) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Search in fullName, phoneNumber, userId
            if (query.getSearch() != null && !query.getSearch().isBlank()) {
                String searchPattern = "%" + query.getSearch().toLowerCase() + "%";
                Predicate fullNamePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("fullName")), searchPattern
                );
                Predicate phoneNumberPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("phoneNumber")), searchPattern
                );
                Predicate userIdPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("userId")), searchPattern
                );
                predicates.add(criteriaBuilder.or(fullNamePredicate, phoneNumberPredicate, userIdPredicate));
            }

            // Filter by accountType
            if (query.getAccountType() != null && !query.getAccountType().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("accountType"), query.getAccountType()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
