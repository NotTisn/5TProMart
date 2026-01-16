package com.fivetpromart.infrastructure.persistence.supplier.spec;

import com.fivetpromart.application.dto.query.SupplierSearchQuery;
import com.fivetpromart.infrastructure.persistence.supplier.SupplierDbo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SupplierSpecification {

    public static Specification<SupplierDbo> getSpec(SupplierSearchQuery query) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // SEARCH: Tìm kiếm trong supplierName HOẶC supplierId (OR logic)
            if (query.getSearch() != null && !query.getSearch().isBlank()) {
                String searchTerm = query.getSearch().trim();
                String searchPattern = "%" + searchTerm.toLowerCase() + "%";
                
                //log.debug("Supplier search - Original term: '{}', Pattern: '{}'", searchTerm, searchPattern);
                
                Predicate supplierNamePredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("supplierName")),
                    searchPattern
                );
                
                Predicate supplierIdPredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("supplierId")),
                    searchPattern
                );
                
                // Tìm trong supplierName HOẶC supplierId
                predicates.add(criteriaBuilder.or(supplierNamePredicate, supplierIdPredicate));
            }

            // FILTER 1: Lọc theo supplierType (Exact match)
            if (query.getSupplierType() != null && !query.getSupplierType().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("supplierType"), query.getSupplierType()));
            }

            // FILTER 2: Lọc theo phoneNumber (Exact match)
            if (query.getPhoneNumber() != null && !query.getPhoneNumber().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("phoneNumber"), query.getPhoneNumber()));
            }

            // FILTER 3: Lọc theo address (Contains)
            if (query.getAddress() != null && !query.getAddress().isBlank()) {
                String addressPattern = "%" + query.getAddress().toLowerCase().trim() + "%";
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("address")),
                    addressPattern
                ));
            }

            // Kết hợp tất cả predicates với AND logic
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}