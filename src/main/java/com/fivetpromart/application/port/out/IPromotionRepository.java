package com.fivetpromart.application.port.out;

import com.fivetpromart.application.dto.query.PromotionSearchQuery;
import com.fivetpromart.domain.model.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IPromotionRepository {
    Promotion save(Promotion promotion);
    Optional<Promotion> findById(String promotionId);
    Optional<Promotion> findByIdIncludingDeleted(String promotionId);
    Page<Promotion> searchPromotions(PromotionSearchQuery query, Pageable pageable);
    void deleteById(String promotionId);
    boolean existsById(String promotionId);
}
