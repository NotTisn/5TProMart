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

        // 1. Tạo đối tượng CHA (PromotionDbo) trước
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
                .isActive(true) // Đảm bảo set giá trị này (hoặc lấy từ domain nếu có)
                .build();

        // 2. Map danh sách CON và truyền 'dbo' (CHA) vào để gán quan hệ
        if (domain.getProducts() != null) {
            List<PromotionProductDbo> productDbos = domain.getProducts().stream()
                    .map(p -> mapProductToDbo(p, dbo)) // <--- QUAN TRỌNG: Truyền 'dbo' vào đây
                    .collect(Collectors.toList());

            // Nếu bạn đã thêm helper method setProducts trong Entity thì dùng nó,
            // còn không thì dùng setter thường (nhưng nhớ đảm bảo list không null)
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

    // 3. Sửa tham số: Nhận 'PromotionDbo parent' thay vì String ID
    default PromotionProductDbo mapProductToDbo(PromotionProduct domain, PromotionDbo parent) {
        if (domain == null) return null;

        return PromotionProductDbo.builder()
                .promotion(parent) // <--- QUAN TRỌNG: Hibernate cần object này để lấy ID cha
                .productId(domain.getProductId())
                .productName(domain.getProductName())
                .build();
    }

    default PromotionProduct mapProductToDomain(PromotionProductDbo dbo) {
        if (dbo == null) return null;

        return PromotionProduct.builder()
                .productId(dbo.getProductId())
                .productName(dbo.getProductName())
                .build();
    }
}