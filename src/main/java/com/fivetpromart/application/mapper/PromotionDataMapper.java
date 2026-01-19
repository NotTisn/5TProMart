package com.fivetpromart.application.mapper;

import com.fivetpromart.application.dto.PromotionDto;
import com.fivetpromart.application.dto.PromotionProductDto;
import com.fivetpromart.domain.model.Promotion;
import com.fivetpromart.domain.model.PromotionProduct;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PromotionDataMapper {

    default PromotionDto toDto(Promotion domain) {
        if (domain == null) return null;

        List<PromotionProductDto> productDtos = domain.getProducts() != null ?
                domain.getProducts().stream()
                        .map(this::mapProductToDto)
                        .collect(Collectors.toList()) : null;

        return PromotionDto.builder()
                .promotionId(domain.getPromotionId())
                .promotionName(domain.getPromotionName())
                .promotionDescription(domain.getPromotionDescription())
                .products(productDtos)
                .promotionType(domain.getPromotionType())
                .discountPercent(domain.getDiscountPercent())
                .buyQuantity(domain.getBuyQuantity())
                .getQuantity(domain.getGetQuantity())
                .startDate(domain.getStartDate())
                .endDate(domain.getEndDate())
                .status(domain.getStatus())
                .promotionStrategy(domain.getPromotionStrategy())
                .build();
    }

    default PromotionProductDto mapProductToDto(PromotionProduct product) {
        return PromotionProductDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .build();
    }
}
