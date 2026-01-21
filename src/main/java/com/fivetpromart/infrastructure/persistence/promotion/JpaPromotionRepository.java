package com.fivetpromart.infrastructure.persistence.promotion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaPromotionRepository extends JpaRepository<PromotionDbo, String>, JpaSpecificationExecutor<PromotionDbo> {
    
    /**
     * Find promotion by ID, only active
     */
    @Query("SELECT p FROM PromotionDbo p WHERE p.promotionId = :promotionId AND p.isActive = true")
    Optional<PromotionDbo> findByPromotionIdAndIsActiveTrue(@Param("promotionId") String promotionId);
    
    /**
     * Find all active promotions
     */
    @Query("SELECT p FROM PromotionDbo p WHERE p.isActive = true")
    List<PromotionDbo> findAllActive();
    
    /**
     * Search active promotions
     */
    @Query("SELECT p FROM PromotionDbo p WHERE LOWER(p.promotionName) LIKE LOWER(CONCAT('%', :keyword, '%')) AND p.isActive = true")
    List<PromotionDbo> searchActivePromotions(@Param("keyword") String keyword);
}

