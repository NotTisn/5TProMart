package com.fivetpromart.application.port.in;

import com.fivetpromart.application.dto.PromotionDto;
import com.fivetpromart.application.dto.command.PromotionCreationCommand;
import com.fivetpromart.application.dto.query.PromotionSearchQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPromotionUseCasePort {
    Page<PromotionDto> searchPromotions(PromotionSearchQuery query, Pageable pageable);
//    PromotionDto getPromotionById(String promotionId);
//    PromotionDto createPromotion(PromotionCreationCommand command);
//    PromotionDto cancelPromotion(String promotionId);
}
