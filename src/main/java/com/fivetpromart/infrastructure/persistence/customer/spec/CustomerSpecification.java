package com.fivetpromart.infrastructure.persistence.customer.spec;

import com.fivetpromart.application.dto.query.CustomerSearchQuery;
import com.fivetpromart.infrastructure.persistence.customer.CustomerDbo;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CustomerSpecification {
    public static Specification<CustomerDbo> getCustomerSpecification(CustomerSearchQuery query){
        return((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Lọc theo id
            if (query.getCustomerId() != null && !query.getCustomerId().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("customerId"), query.getCustomerId()));
            }

            // 2. Lọc theo name
            if (query.getCustomerName() != null && !query.getCustomerName().isBlank()) {
                // lower(customerName) like %value%
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("productName")),
                        "%" + query.getCustomerName().toLowerCase() + "%"
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
