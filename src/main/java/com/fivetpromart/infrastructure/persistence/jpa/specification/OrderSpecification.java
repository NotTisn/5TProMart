package com.fivetpromart.infrastructure.persistence.jpa.specification;

import com.fivetpromart.application.dto.query.OrderSearchQuery;
import com.fivetpromart.infrastructure.persistence.jpa.entity.OrderDbo;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class OrderSpecification {

    /**
     * Build dynamic query specification from OrderSearchQuery
     */
    public static Specification<OrderDbo> getSpecification(OrderSearchQuery query) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Search in orderId, customer name (join needed), or customerId
            if (query.getSearch() != null && !query.getSearch().isBlank()) {
                String searchPattern = "%" + query.getSearch().toLowerCase() + "%";
                Predicate searchPredicate = criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("orderId")), searchPattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("customerId")), searchPattern)
                        // TODO: Add join to customer table to search by customer name
                        // criteriaBuilder.like(criteriaBuilder.lower(root.join("customer").get("fullName")), searchPattern)
                );
                predicates.add(searchPredicate);
            }

            // Filter by staffId
            if (query.getStaffId() != null && !query.getStaffId().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("staffId"), query.getStaffId()));
            }

            // Filter by date range
            if (query.getStartDate() != null) {
                LocalDateTime startDateTime = query.getStartDate().atStartOfDay();
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("orderDate"), startDateTime));
            }
            if (query.getEndDate() != null) {
                LocalDateTime endDateTime = query.getEndDate().atTime(LocalTime.MAX);
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("orderDate"), endDateTime));
            }

            // Filter by payment method
            if (query.getPaymentMethod() != null && !query.getPaymentMethod().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("paymentMethod"), query.getPaymentMethod()));
            }

            // Filter by status
            if (query.getStatus() != null && !query.getStatus().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), query.getStatus()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
