package com.fivetpromart.infrastructure.persistence.jpa.repository;

import com.fivetpromart.infrastructure.persistence.jpa.entity.OrderDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderJpaRepository extends JpaRepository<OrderDbo, String>, JpaSpecificationExecutor<OrderDbo> {
    // Custom queries can be added here if needed
}
