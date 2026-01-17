package com.fivetpromart.infrastructure.persistence.promotion.mapper;

import com.fivetpromart.domain.model.Promotion;
import com.fivetpromart.domain.model.PromotionProduct;
import com.fivetpromart.infrastructure.persistence.promotion.PromotionDbo;
import com.fivetpromart.infrastructure.persistence.promotion.PromotionProductDbo;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PromotionPersistenceMapper {

    default PromotionDbo toDbo(Promotion domain) {
        if (domain == null) return null;

        PromotionDbo dbo = PromotionDbo.builder()
                .promotionId(domain.getPromotionId())
                .promotionName(domain.getPromotionName())
                .promotionDescription(domain.getPromotionDescription())
                .promotionType(domain.getPromotionType())
                .discountPercent(domain.getDiscountPercent())
                .buyQuantity(domain.getBuyQuantity())
                .getQuantity(domain.getGetQuantity())
                .startDate(domain.getStartDate())
                .endDate(domain.getEndDate())
                .status(domain.getStatus())
                .build();

        if (domain.getProducts() != null) {
            List<PromotionProductDbo> productDbos = domain.getProducts().stream()
                    .map(p -> mapProductToDbo(p, domain.getPromotionId()))
                    .collect(Collectors.toList());
            dbo.setProducts(productDbos);
        }

        return dbo;
    }

    default Promotion toDomain(PromotionDbo dbo) {
        if (dbo == null) return null;

        List<PromotionProduct> products = dbo.getProducts() != null ?
                dbo.getProducts().stream()
                        .map(this::mapProductToDomain)
                        .collect(Collectors.toList()) : null;

        return Promotion.reconstitute(
                dbo.getPromotionId(),
                dbo.getPromotionName(),
                dbo.getPromotionDescription(),
                products,
                dbo.getPromotionType(),
                dbo.getDiscountPercent(),
                dbo.getBuyQuantity(),
                dbo.getGetQuantity(),
                dbo.getStartDate(),
                dbo.getEndDate(),
                dbo.getStatus()
        );
    }

    default PromotionProductDbo mapProductToDbo(PromotionProduct product, String promotionId) {
        return PromotionProductDbo.builder()
                .promotionId(promotionId)
                .productId(product.getProductId())
                .productName(product.getProductName())
                .build();
    }

    default PromotionProduct mapProductToDomain(PromotionProductDbo dbo) {
        return PromotionProduct.builder()
                .productId(dbo.getProductId())
                .productName(dbo.getProductName())
                .build();
    }
}
