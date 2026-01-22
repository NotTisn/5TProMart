package com.fivetpromart.application.dto.analytics;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTOs for Analytics responses from the Python AI service.
 * These mirror the Pydantic models in promart-ai-service.
 */
public class AnalyticsDtos {

    // ========================================================================
    // MARGIN OPTIMIZER DTOs
    // ========================================================================

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MarginAlert {
        private String type;           // NEGATIVE_MARGIN, THIN_MARGIN, EXPIRY_RISK
        private String severity;       // CRITICAL, HIGH, MEDIUM, LOW
        @JsonProperty("product_id")
        private String productId;
        @JsonProperty("product_name")
        private String productName;
        @JsonProperty("current_price")
        private Double currentPrice;
        @JsonProperty("cost_price")
        private Double costPrice;
        @JsonProperty("margin_percent")
        private Double marginPercent;
        @JsonProperty("units_at_risk")
        private Integer unitsAtRisk;
        @JsonProperty("expiry_date")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate expiryDate;
        @JsonProperty("days_remaining")
        private Integer daysRemaining;
        @JsonProperty("cost_value")
        private Double costValue;
        private String suggestion;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MarginSummary {
        @JsonProperty("total_products")
        private int totalProducts;
        @JsonProperty("healthy_margins")
        private int healthyMargins;
        @JsonProperty("thin_margins")
        private int thinMargins;
        @JsonProperty("negative_margins")
        private int negativeMargins;
        @JsonProperty("at_risk_of_waste")
        private int atRiskOfWaste;
        @JsonProperty("total_potential_waste_value")
        private double totalPotentialWasteValue;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MarginInsightResponse {
        @JsonProperty("data_quality")
        private String dataQuality;
        private MarginSummary summary;
        private List<MarginAlert> alerts;
        @JsonProperty("generated_at")
        private LocalDateTime generatedAt;
    }

    // ========================================================================
    // DEMAND INTELLIGENCE DTOs
    // ========================================================================

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataRange {
        @JsonProperty("from_date")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate fromDate;
        @JsonProperty("to_date")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate toDate;
        @JsonProperty("days_of_data")
        private int daysOfData;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WeeklyPattern {
        private Double monday;
        private Double tuesday;
        private Double wednesday;
        private Double thursday;
        private Double friday;
        private Double saturday;
        private Double sunday;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Forecast {
        @JsonProperty("next_7_days")
        private Integer next7Days;
        @JsonProperty("next_14_days")
        private Integer next14Days;
        @JsonProperty("next_30_days")
        private Integer next30Days;
        private Double confidence;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DemandInsightResponse {
        @JsonProperty("product_id")
        private String productId;
        @JsonProperty("product_name")
        private String productName;
        @JsonProperty("data_quality")
        private String dataQuality;
        @JsonProperty("current_stock")
        private Integer currentStock;
        @JsonProperty("average_daily_sales")
        private Double averageDailySales;
        @JsonProperty("days_until_stockout")
        private Integer daysUntilStockout;
        @JsonProperty("suggested_reorder_point")
        private Integer suggestedReorderPoint;
        @JsonProperty("suggested_reorder_quantity")
        private Integer suggestedReorderQuantity;
        @JsonProperty("weekly_pattern")
        private WeeklyPattern weeklyPattern;
        private Forecast forecast;
        @JsonProperty("insight_message")
        private String insightMessage;
        @JsonProperty("generated_at")
        private LocalDateTime generatedAt;
        @JsonProperty("data_range")
        private DataRange dataRange;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReorderAlert {
        @JsonProperty("product_id")
        private String productId;
        @JsonProperty("product_name")
        private String productName;
        @JsonProperty("current_stock")
        private int currentStock;
        @JsonProperty("days_until_stockout")
        private int daysUntilStockout;
        private String urgency;       // CRITICAL, HIGH, MEDIUM, LOW
        @JsonProperty("suggested_quantity")
        private int suggestedQuantity;
    }

    // ========================================================================
    // BUNDLE INSIGHTS DTOs
    // ========================================================================

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssociationRule {
        private List<String> antecedent;
        private List<String> consequent;
        private double support;
        private double confidence;
        private double lift;
        private String interpretation;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlacementSuggestion {
        private String product;
        @JsonProperty("suggest_near")
        private String suggestNear;
        private String reason;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BundleInsightResponse {
        @JsonProperty("data_quality")
        private String dataQuality;
        @JsonProperty("total_orders_analyzed")
        private int totalOrdersAnalyzed;
        @JsonProperty("multi_item_orders_analyzed")
        private int multiItemOrdersAnalyzed;
        private List<AssociationRule> rules;
        @JsonProperty("placement_suggestions")
        private List<PlacementSuggestion> placementSuggestions;
        @JsonProperty("insight_message")
        private String insightMessage;
        @JsonProperty("generated_at")
        private LocalDateTime generatedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BundleDataStatus {
        @JsonProperty("ready_for_analysis")
        private boolean readyForAnalysis;
        @JsonProperty("multi_item_orders")
        private int multiItemOrders;
        @JsonProperty("required_orders")
        private int requiredOrders;
        @JsonProperty("orders_needed")
        private int ordersNeeded;
        @JsonProperty("progress_percent")
        private int progressPercent;
        @JsonProperty("first_order_date")
        private String firstOrderDate;
        @JsonProperty("last_order_date")
        private String lastOrderDate;
        private String message;
    }

    // ========================================================================
    // HEALTH CHECK DTO
    // ========================================================================

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnalyticsHealthResponse {
        private String status;
        private String service;
        private String version;
        private LocalDateTime timestamp;
    }
}
