package com.fivetpromart.application.dto.analytics;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
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
        @JsonAlias("product_id")
        private String productId;
        @JsonAlias("product_name")
        private String productName;
        @JsonAlias("current_price")
        private Double currentPrice;
        @JsonAlias("cost_price")
        private Double costPrice;
        @JsonAlias("margin_percent")
        private Double marginPercent;
        @JsonAlias("units_at_risk")
        private Integer unitsAtRisk;
        @JsonAlias("expiry_date")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate expiryDate;
        @JsonAlias("days_remaining")
        private Integer daysRemaining;
        @JsonAlias("cost_value")
        private Double costValue;
        private String suggestion;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MarginSummary {
        @JsonAlias("total_products")
        private int totalProducts;
        @JsonAlias("healthy_margins")
        private int healthyMargins;
        @JsonAlias("thin_margins")
        private int thinMargins;
        @JsonAlias("negative_margins")
        private int negativeMargins;
        @JsonAlias("at_risk_of_waste")
        private int atRiskOfWaste;
        @JsonAlias("total_potential_waste_value")
        private double totalPotentialWasteValue;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MarginInsightResponse {
        @JsonAlias("data_quality")
        private String dataQuality;
        private MarginSummary summary;
        private List<MarginAlert> alerts;
        @JsonAlias("generated_at")
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
        @JsonAlias("from_date")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate fromDate;
        @JsonAlias("to_date")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate toDate;
        @JsonAlias("days_of_data")
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
        @JsonAlias("next_7_days")
        private Integer next7Days;
        @JsonAlias("next_14_days")
        private Integer next14Days;
        @JsonAlias("next_30_days")
        private Integer next30Days;
        private Double confidence;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DemandInsightResponse {
        @JsonAlias("product_id")
        private String productId;
        @JsonAlias("product_name")
        private String productName;
        @JsonAlias("data_quality")
        private String dataQuality;
        @JsonAlias("current_stock")
        private Integer currentStock;
        @JsonAlias("average_daily_sales")
        private Double averageDailySales;
        @JsonAlias("days_until_stockout")
        private Integer daysUntilStockout;
        @JsonAlias("suggested_reorder_point")
        private Integer suggestedReorderPoint;
        @JsonAlias("suggested_reorder_quantity")
        private Integer suggestedReorderQuantity;
        @JsonAlias("weekly_pattern")
        private WeeklyPattern weeklyPattern;
        private Forecast forecast;
        @JsonAlias("insight_message")
        private String insightMessage;
        @JsonAlias("generated_at")
        private LocalDateTime generatedAt;
        @JsonAlias("data_range")
        private DataRange dataRange;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReorderAlert {
        @JsonAlias("product_id")
        private String productId;
        @JsonAlias("product_name")
        private String productName;
        @JsonAlias("current_stock")
        private int currentStock;
        @JsonAlias("days_until_stockout")
        private int daysUntilStockout;
        private String urgency;       // CRITICAL, HIGH, MEDIUM, LOW
        @JsonAlias("suggested_quantity")
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
        @JsonAlias("suggest_near")
        private String suggestNear;
        private String reason;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BundleInsightResponse {
        @JsonAlias("data_quality")
        private String dataQuality;
        @JsonAlias("total_orders_analyzed")
        private int totalOrdersAnalyzed;
        @JsonAlias("multi_item_orders_analyzed")
        private int multiItemOrdersAnalyzed;
        private List<AssociationRule> rules;
        @JsonAlias("placement_suggestions")
        private List<PlacementSuggestion> placementSuggestions;
        @JsonAlias("insight_message")
        private String insightMessage;
        @JsonAlias("generated_at")
        private LocalDateTime generatedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BundleDataStatus {
        @JsonAlias("ready_for_analysis")
        private boolean readyForAnalysis;
        @JsonAlias("multi_item_orders")
        private int multiItemOrders;
        @JsonAlias("required_orders")
        private int requiredOrders;
        @JsonAlias("orders_needed")
        private int ordersNeeded;
        @JsonAlias("progress_percent")
        private int progressPercent;
        @JsonAlias("first_order_date")
        private String firstOrderDate;
        @JsonAlias("last_order_date")
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
