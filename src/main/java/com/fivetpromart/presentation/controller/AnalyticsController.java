package com.fivetpromart.presentation.controller;

import com.fivetpromart.application.dto.analytics.AnalyticsDtos.*;
import com.fivetpromart.infrastructure.analytics.AnalyticsProxyService;
import com.fivetpromart.presentation.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for AI-powered analytics endpoints.
 * Proxies requests to the Python AI service with auth and caching.
 * 
 * Access: Manager, Admin, WarehouseStaff (read-only analytics)
 */
@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
@Slf4j
public class AnalyticsController {

    private final AnalyticsProxyService analyticsService;

    // ========================================================================
    // MARGIN OPTIMIZER ENDPOINTS
    // ========================================================================

    /**
     * Get margin health overview and alerts.
     * Works Day 1 — no historical data required.
     */
    @GetMapping("/margins")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('Admin', 'Manager', 'WarehouseStaff')")
    public ApiResponse<MarginInsightResponse> getMarginInsights(
            @RequestParam(required = false, defaultValue = "0.15") Double threshold,
            @RequestParam(required = false, defaultValue = "30") Integer expiryDays
    ) {
        log.info("Fetching margin insights: threshold={}, expiryDays={}", threshold, expiryDays);
        
        MarginInsightResponse insights = analyticsService.getMarginInsights(threshold, expiryDays);
        
        return ApiResponse.<MarginInsightResponse>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Margin insights retrieved successfully")
                .data(insights)
                .build();
    }

    /**
     * Get margin alerts only (for dashboard widgets).
     */
    @GetMapping("/margins/alerts")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('Admin', 'Manager', 'WarehouseStaff')")
    public ApiResponse<List<MarginAlert>> getMarginAlerts(
            @RequestParam(required = false) String severity
    ) {
        List<MarginAlert> alerts = analyticsService.getMarginAlerts(severity);
        
        return ApiResponse.<List<MarginAlert>>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Margin alerts retrieved")
                .data(alerts)
                .build();
    }

    // ========================================================================
    // DEMAND INTELLIGENCE ENDPOINTS
    // ========================================================================

    /**
     * Get demand intelligence for a specific product.
     * Response adapts to data quality (INSUFFICIENT → MINIMAL → SUFFICIENT → RICH)
     */
    @GetMapping("/demand/{productId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('Admin', 'Manager', 'WarehouseStaff')")
    public ApiResponse<DemandInsightResponse> getDemandInsight(
            @PathVariable String productId
    ) {
        log.info("Fetching demand insight for product: {}", productId);
        
        DemandInsightResponse insight = analyticsService.getDemandInsight(productId);
        
        return ApiResponse.<DemandInsightResponse>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Demand insight retrieved for product " + productId)
                .data(insight)
                .build();
    }

    /**
     * Get products that need reordering.
     * Sorted by urgency (days until stockout).
     */
    @GetMapping("/demand/reorder-alerts")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('Admin', 'Manager', 'WarehouseStaff')")
    public ApiResponse<List<ReorderAlert>> getReorderAlerts(
            @RequestParam(required = false, defaultValue = "14") Integer thresholdDays
    ) {
        log.info("Fetching reorder alerts: thresholdDays={}", thresholdDays);
        
        List<ReorderAlert> alerts = analyticsService.getReorderAlerts(thresholdDays);
        
        return ApiResponse.<List<ReorderAlert>>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Reorder alerts retrieved")
                .data(alerts)
                .build();
    }

    // ========================================================================
    // BUNDLE INSIGHTS ENDPOINTS
    // ========================================================================

    /**
     * Get product association rules and bundle insights.
     * Requires sufficient multi-item order history.
     */
    @GetMapping("/bundles")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('Admin', 'Manager')")
    public ApiResponse<BundleInsightResponse> getBundleInsights(
            @RequestParam(required = false) Double minSupport,
            @RequestParam(required = false) Double minConfidence,
            @RequestParam(required = false) Integer limit
    ) {
        log.info("Fetching bundle insights");
        
        BundleInsightResponse insights = analyticsService.getBundleInsights(
                minSupport, minConfidence, limit);
        
        return ApiResponse.<BundleInsightResponse>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Bundle insights retrieved")
                .data(insights)
                .build();
    }

    /**
     * Check data readiness for bundle analysis.
     * Use this to show progress toward enabling bundle features.
     */
    @GetMapping("/bundles/data-status")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('Admin', 'Manager')")
    public ApiResponse<BundleDataStatus> getBundleDataStatus() {
        BundleDataStatus status = analyticsService.getBundleDataStatus();
        
        return ApiResponse.<BundleDataStatus>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Bundle data status retrieved")
                .data(status)
                .build();
    }

    // ========================================================================
    // HEALTH & STATUS ENDPOINTS
    // ========================================================================

    /**
     * Check if the analytics AI service is available.
     * Returns the health response from the Python AI service.
     */
    @GetMapping("/health")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<AnalyticsHealthResponse> getAnalyticsHealth() {
        log.debug("Checking analytics service health");
        
        AnalyticsHealthResponse health = analyticsService.getHealthResponse();
        boolean isHealthy = "healthy".equals(health.getStatus());
        
        return ApiResponse.<AnalyticsHealthResponse>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message(isHealthy ? "Analytics service is healthy" : "Analytics service is unavailable")
                .data(health)
                .build();
    }
}
