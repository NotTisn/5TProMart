package com.fivetpromart.presentation.dto.request;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PromotionRequest {
    @NotBlank(message = "Promotion name is required.")
    private String promotionName;

    private String promotionDescription;

    // Accept both formats:
    // Discount: ["productId1", "productId2"]
    // Buy X Get Y: [{"productBuy": "id1", "productGet": "id2"}]
    @NotNull(message = "Products list is required.")
    private JsonNode products;

    @NotBlank(message = "Promotion type is required.")
    private String promotionType;

    private Integer discountPercent;
    private Integer buyQuantity;
    private Integer getQuantity;

    @NotNull(message = "Start date is required.")
    private LocalDate startDate;

    @NotNull(message = "End date is required.")
    private LocalDate endDate;
    
    /**
     * Helper method to extract product IDs based on promotion type
     * For Discount: products is array of strings
     * For Buy X Get Y: products is array of objects with productBuy/productGet
     */
    public List<String> getProductIdsForProcessing() {
        List<String> productIds = new ArrayList<>();
        
        if (products == null || !products.isArray()) {
            return productIds;
        }
        
        if ("Discount".equals(promotionType)) {
            // For Discount: ["productId1", "productId2"]
            for (JsonNode node : products) {
                if (node.isTextual()) {
                    productIds.add(node.asText());
                }
            }
        } else if ("Buy X Get Y".equals(promotionType)) {
            // For Buy X Get Y: [{"productBuy": "id1", "productGet": "id2"}]
            for (JsonNode node : products) {
                if (node.isObject() && node.has("productBuy")) {
                    productIds.add(node.get("productBuy").asText());
                }
            }
        }
        
        return productIds;
    }
    
    /**
     * Get product pairs for Buy X Get Y promotions
     */
    public List<BuyXGetYProductRequest> getProductPairs() {
        List<BuyXGetYProductRequest> pairs = new ArrayList<>();
        
        if (products == null || !products.isArray() || !"Buy X Get Y".equals(promotionType)) {
            return pairs;
        }
        
        for (JsonNode node : products) {
            if (node.isObject() && node.has("productBuy") && node.has("productGet")) {
                BuyXGetYProductRequest pair = new BuyXGetYProductRequest();
                pair.setProductBuy(node.get("productBuy").asText());
                pair.setProductGet(node.get("productGet").asText());
                pairs.add(pair);
            }
        }
        
        return pairs;
    }
}
