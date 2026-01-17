package com.fivetpromart.infrastructure.persistence.promotion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPromotionRepository extends JpaRepository<PromotionDbo, String>, JpaSpecificationExecutor<PromotionDbo> {
}
