package com.fivetpromart.presentation.controller;

import com.fivetpromart.application.dto.ProductDto;
import com.fivetpromart.application.dto.PromotionDto;
import com.fivetpromart.application.dto.PromotionProductDto;
import com.fivetpromart.application.dto.command.PromotionCreationCommand;
import com.fivetpromart.application.dto.query.PromotionSearchQuery;
import com.fivetpromart.application.usecase.ProductUseCase;
import com.fivetpromart.application.usecase.PromotionUseCase;
import com.fivetpromart.presentation.dto.request.PromotionRequest;
import com.fivetpromart.presentation.dto.response.*;
import com.fivetpromart.presentation.mapper.PromotionPresentationMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionUseCase promotionUseCase;
    private final ProductUseCase productUseCase;
    private final PromotionPresentationMapper mapper;

    /**
     * 1.1 Get promotions query
     * GET /api/v1/promotions
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('Admin', 'Manager')")
    public ApiResponse<List<PromotionResponse>> searchPromotions(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false, defaultValue = "false") Boolean includeDeleted,
            @RequestParam(defaultValue = "startDate") String sortBy,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PromotionSearchQuery query = PromotionSearchQuery.builder()
                .search(search)
                .type(type)
                .status(status)
                .includeDeleted(includeDeleted)
                .sortBy(sortBy)
                .order(order)
                .build();

        Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<PromotionDto> promotionPage = promotionUseCase.searchPromotions(query, pageable);

        List<PromotionResponse> responses = promotionPage.getContent().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        PaginationMeta meta = PaginationMeta.builder()
                .totalItems(promotionPage.getTotalElements())
                .itemsPerPage(promotionPage.getSize())
                .totalPages(promotionPage.getTotalPages())
                .startPage(promotionPage.getNumber())
                .build();

        return ApiResponse.<List<PromotionResponse>>builder()
                .success(true)
                .message("Get promotions list successfully.")
                .data(responses)
                .pagination(meta)
                .build();
    }

    /**
     * 1.2 Get promotion detail
     * GET /api/v1/promotions/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('Admin', 'Manager')")
    public ApiResponse<PromotionDetailResponse> getPromotionById(@PathVariable String id) {
        PromotionDto promotion = promotionUseCase.getPromotionById(id);

        // Fetch product details and calculate promotion price using strategy
        List<PromotionDetailProductResponse> detailProducts = new ArrayList<>();
        for (PromotionProductDto promotionProduct : promotion.getProducts()) {
            try {
                ProductDto product = productUseCase.getProductById(promotionProduct.getProductId());

                // Use promotion strategy to calculate promotional price
                BigDecimal promotionPrice = null;
                if ("Discount".equals(promotion.getPromotionType()) && promotion.getDiscountPercent() != null) {
                    // Strategy will handle the calculation
                    promotionPrice = promotion.getPromotionStrategy()
                            .calculatePromotionalPrice(product.getSellingPrice(), 1);
                }

                PromotionDetailProductResponse detailProduct = PromotionDetailProductResponse.builder()
                        .productId(product.getProductId())
                        .productName(product.getProductName())
                        .unitOfMeasure(product.getUnitOfMeasure())
                        .sellingPrice(product.getSellingPrice())
                        .promotionPrice(promotionPrice)
                        .build();

                detailProducts.add(detailProduct);
            } catch (Exception e) {
                // Skip products that don't exist
                continue;
            }
        }

        PromotionDetailResponse response = PromotionDetailResponse.builder()
                .promotionId(promotion.getPromotionId())
                .promotionName(promotion.getPromotionName())
                .promotionDescription(promotion.getPromotionDescription())
                .promotionType(promotion.getPromotionType())
                .discountPercent(promotion.getDiscountPercent())
                .buyQuantity(promotion.getBuyQuantity())
                .getQuantity(promotion.getGetQuantity())
                .status(promotion.getStatus())
                .startDate(promotion.getStartDate())
                .endDate(promotion.getEndDate())
                .products(detailProducts)
                .build();

        return ApiResponse.<PromotionDetailResponse>builder()
                .success(true)
                .message("Get promotions detail successfully.")
                .data(response)
                .build();
    }

    /**
     * 1.3 Create Promotion
     * POST /api/v1/promotions
     */
    @PostMapping
    @PreAuthorize("hasRole('Admin')")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PromotionResponse> createPromotion(
            @Valid @RequestBody PromotionRequest request
    ) {
        PromotionCreationCommand command = mapper.toCommand(request);
        PromotionDto dto = promotionUseCase.createPromotion(command);

        // Create simplified response for creation
        PromotionResponse response = PromotionResponse.builder()
                .promotionId(dto.getPromotionId())
                .promotionName(dto.getPromotionName())
                .promotionDescription(dto.getPromotionDescription())
                .status(dto.getStatus())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();

        return ApiResponse.<PromotionResponse>builder()
                .success(true)
                .statusCode(HttpStatus.CREATED.value())
                .message("Promotion created successfully.")
                .data(response)
                .build();
    }

    /**
     * 1.4 Cancel promotion
     * PUT /api/v1/promotions/{id}/cancel
     */
    @PutMapping("/{id}/cancel")
    public ApiResponse<PromotionResponse> cancelPromotion(@PathVariable String id) {
        PromotionDto dto = promotionUseCase.cancelPromotion(id);

        PromotionResponse response = PromotionResponse.builder()
                .promotionId(dto.getPromotionId())
                .status(dto.getStatus())
                .build();

        return ApiResponse.<PromotionResponse>builder()
                .success(true)
                .message("Promotion cancelled.")
                .data(response)
                .build();
    }

    /**
     * 1.5 Restore promotion
     * POST /api/v1/promotions/{id}/restore
     */
    @PostMapping("/{id}/restore")
    @PreAuthorize("hasRole('Admin')")
    public ApiResponse<PromotionResponse> restorePromotion(@PathVariable String id) {
        PromotionDto dto = promotionUseCase.restorePromotion(id);

        PromotionResponse response = mapper.toResponse(dto);

        return ApiResponse.<PromotionResponse>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Successfully restored promotion")
                .data(response)
                .build();
    }
}
