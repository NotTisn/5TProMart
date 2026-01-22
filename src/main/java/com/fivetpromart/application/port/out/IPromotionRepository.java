package com.fivetpromart.application.port.out;

import com.fivetpromart.application.dto.query.PromotionSearchQuery;
import com.fivetpromart.domain.model.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IPromotionRepository {
    Promotion save(Promotion promotion);
    Optional<Promotion> findById(String promotionId);
    Optional<Promotion> findByIdIncludingDeleted(String promotionId);
    Page<Promotion> searchPromotions(PromotionSearchQuery query, Pageable pageable);
    void deleteById(String promotionId);
    boolean existsById(String promotionId);
    
    /**
     * Find all active promotions that apply to a specific product.
     * Active = status is "Active" AND current date is between startDate and endDate.
     * @param productId the product ID to search for
     * @return list of active promotions for this product (may be empty)
     */
    List<Promotion> findActivePromotionsByProductId(String productId);
}
