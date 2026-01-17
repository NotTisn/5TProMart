package com.fivetpromart.application.usecase;

import com.fivetpromart.application.dto.PromotionDto;
import com.fivetpromart.application.dto.command.PromotionCreationCommand;
import com.fivetpromart.application.dto.query.PromotionSearchQuery;
import com.fivetpromart.application.mapper.PromotionDataMapper;
import com.fivetpromart.application.port.in.IPromotionUseCasePort;
import com.fivetpromart.application.port.out.IProductRepository;
import com.fivetpromart.application.port.out.IPromotionRepository;
import com.fivetpromart.domain.exception.EmptyFieldException;
import com.fivetpromart.domain.model.Product;
import com.fivetpromart.domain.model.Promotion;
import com.fivetpromart.domain.model.PromotionProduct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PromotionUseCase implements IPromotionUseCasePort {

    private final IPromotionRepository promotionRepository;
    private final IProductRepository productRepository;
    private final PromotionDataMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<PromotionDto> searchPromotions(PromotionSearchQuery query, Pageable pageable) {
        Page<Promotion> promotions = promotionRepository.searchPromotions(query, pageable);
        return promotions.map(mapper::toDto);
    }

//    @Override
//    @Transactional(readOnly = true)
//    public PromotionDto getPromotionById(String promotionId) {
//        Promotion promotion = promotionRepository.findById(promotionId)
//                .orElseThrow(() -> new EntityNotFoundException("Promotion not found with ID: " + promotionId));
//        return mapper.toDto(promotion);
//    }
//
//    @Override
//    @Transactional
//    public PromotionDto createPromotion(PromotionCreationCommand command) {
//        // Validate and fetch product details
//        List<PromotionProduct> promotionProducts = new ArrayList<>();
//        for (String productId : command.getProducts()) {
//            Product product = productRepository.findById(productId)
//                    .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));
//
//            promotionProducts.add(PromotionProduct.builder()
//                    .productId(product.getProductId())
//                    .productName(product.getProductName())
//                    .build());
//        }
//
//        // Validate promotion type specific fields
//        if ("Discount".equals(command.getPromotionType())) {
//            if (command.getDiscountPercent() == null || command.getDiscountPercent() < 1 || command.getDiscountPercent() > 100) {
//                throw new IllegalArgumentException("Discount percent must be between 1 and 100.");
//            }
//        } else if ("Buy X Get Y".equals(command.getPromotionType())) {
//            if (command.getBuyQuantity() == null || command.getBuyQuantity() <= 0) {
//                throw new IllegalArgumentException("Buy quantity must be greater than 0");
//            }
//            if (command.getGetQuantity() == null || command.getGetQuantity() <= 0) {
//                throw new IllegalArgumentException("Get quantity must be greater than 0");
//            }
//        }
//
//        // Create promotion
//        Promotion promotion = Promotion.create(
//                command.getPromotionName(),
//                command.getPromotionDescription(),
//                promotionProducts,
//                command.getPromotionType(),
//                command.getDiscountPercent(),
//                command.getBuyQuantity(),
//                command.getGetQuantity(),
//                command.getStartDate(),
//                command.getEndDate()
//        );
//
//        Promotion saved = promotionRepository.save(promotion);
//        return mapper.toDto(saved);
//    }
//
//    @Override
//    @Transactional
//    public PromotionDto cancelPromotion(String promotionId) {
//        Promotion promotion = promotionRepository.findById(promotionId)
//                .orElseThrow(() -> new EntityNotFoundException("Promotion not found with ID: " + promotionId));
//
//        promotion.cancel();
//        Promotion saved = promotionRepository.save(promotion);
//        return mapper.toDto(saved);
//    }
}
