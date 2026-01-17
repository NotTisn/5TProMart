package com.fivetpromart.infrastructure.persistence.promotion;

import com.fivetpromart.application.dto.query.PromotionSearchQuery;
import com.fivetpromart.application.port.out.IPromotionRepository;
import com.fivetpromart.domain.model.Promotion;
import com.fivetpromart.infrastructure.persistence.promotion.mapper.PromotionPersistenceMapper;
import com.fivetpromart.infrastructure.persistence.promotion.spec.PromotionSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PromotionRepositoryAdapter implements IPromotionRepository {

    private final JpaPromotionRepository jpaRepository;
    private final PromotionPersistenceMapper mapper;

    @Override
    public Promotion save(Promotion promotion) {
        PromotionDbo dbo = mapper.toDbo(promotion);
        PromotionDbo saved = jpaRepository.save(dbo);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Promotion> findById(String promotionId) {
        return jpaRepository.findById(promotionId)
                .map(mapper::toDomain);
    }

    @Override
    public Page<Promotion> searchPromotions(PromotionSearchQuery query, Pageable pageable) {
        Specification<PromotionDbo> spec = PromotionSpecification.getSpec(query);
        Page<PromotionDbo> dboPage = jpaRepository.findAll(spec, pageable);
        return dboPage.map(mapper::toDomain);
    }

    @Override
    public void deleteById(String promotionId) {
        jpaRepository.deleteById(promotionId);
    }

    @Override
    public boolean existsById(String promotionId) {
        return jpaRepository.existsById(promotionId);
    }
}
