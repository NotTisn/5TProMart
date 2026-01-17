package com.fivetpromart.presentation.mapper;

import com.fivetpromart.application.dto.PromotionDto;
import com.fivetpromart.application.dto.PromotionProductDto;
import com.fivetpromart.application.dto.command.PromotionCreationCommand;
import com.fivetpromart.presentation.dto.request.PromotionRequest;
import com.fivetpromart.presentation.dto.response.PromotionProductResponse;
import com.fivetpromart.presentation.dto.response.PromotionResponse;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PromotionPresentationMapper {

    PromotionCreationCommand toCommand(PromotionRequest request);

    default PromotionResponse toResponse(PromotionDto dto) {
        if (dto == null) return null;

        List<PromotionProductResponse> productResponses = dto.getProducts() != null ?
                dto.getProducts().stream()
                        .map(this::mapProductToResponse)
                        .collect(Collectors.toList()) : null;

        return PromotionResponse.builder()
                .promotionId(dto.getPromotionId())
                .promotionName(dto.getPromotionName())
                .promotionDescription(dto.getPromotionDescription())
                .products(productResponses)
                .promotionType(dto.getPromotionType())
                .discountPercent(dto.getDiscountPercent())
                .buyQuantity(dto.getBuyQuantity())
                .getQuantity(dto.getGetQuantity())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .status(dto.getStatus())
                .build();
    }

    default PromotionProductResponse mapProductToResponse(PromotionProductDto dto) {
        return PromotionProductResponse.builder()
                .productId(dto.getProductId())
                .productName(dto.getProductName())
                .build();
    }
}
