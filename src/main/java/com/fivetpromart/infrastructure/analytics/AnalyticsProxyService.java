package com.fivetpromart.infrastructure.analytics;

import com.fivetpromart.application.dto.analytics.AnalyticsDtos.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service for proxying requests to the Python AI analytics service.
 * Provides caching, error handling, and fallback responses.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsProxyService {

    private final RestTemplate restTemplate;

    @Value("${analytics.service.url:http://localhost:8090}")
    private String analyticsServiceUrl;

    @Value("${analytics.service.enabled:true}")
    private boolean analyticsEnabled;

    // ========================================================================
    // MARGIN OPTIMIZER
    // ========================================================================

    /**
     * Get margin insights from the AI service.
     * @param threshold Margin threshold (default 0.15)
     * @param expiryDays Days to look ahead for expiry (default 30)
     * @return Margin insights or fallback response
     */
    public MarginInsightResponse getMarginInsights(Double threshold, Integer expiryDays) {
        if (!analyticsEnabled) {
            return createFallbackMarginResponse("Analytics service is disabled");
        }

        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl(analyticsServiceUrl + "/internal/margins")
                    .queryParamIfPresent("threshold", Optional.ofNullable(threshold))
                    .queryParamIfPresent("expiry_days", Optional.ofNullable(expiryDays))
                    .toUriString();

            log.debug("Calling analytics service: {}", url);
            ResponseEntity<MarginInsightResponse> response = restTemplate.getForEntity(
                    url, MarginInsightResponse.class);

            return response.getBody();
        } catch (RestClientException e) {
            log.warn("Failed to fetch margin insights from analytics service: {}", e.getMessage());
            return createFallbackMarginResponse("Analytics service unavailable: " + e.getMessage());
        }
    }

    /**
     * Get margin alerts filtered by severity.
     */
    public List<MarginAlert> getMarginAlerts(String severity) {
        if (!analyticsEnabled) {
            return Collections.emptyList();
        }

        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl(analyticsServiceUrl + "/internal/margins/alerts")
                    .queryParamIfPresent("severity", Optional.ofNullable(severity))
                    .toUriString();

            ResponseEntity<List<MarginAlert>> response = restTemplate.exchange(
                    url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<MarginAlert>>() {});

            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (RestClientException e) {
            log.warn("Failed to fetch margin alerts: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    // ========================================================================
    // DEMAND INTELLIGENCE
    // ========================================================================

    /**
     * Get demand insights for a specific product.
     */
    public DemandInsightResponse getDemandInsight(String productId) {
        if (!analyticsEnabled) {
            return createFallbackDemandResponse(productId, "Analytics service is disabled");
        }

        try {
            String url = analyticsServiceUrl + "/internal/demand/" + productId;
            log.debug("Calling analytics service: {}", url);
            
            ResponseEntity<DemandInsightResponse> response = restTemplate.getForEntity(
                    url, DemandInsightResponse.class);

            return response.getBody();
        } catch (RestClientException e) {
            log.warn("Failed to fetch demand insight for product {}: {}", productId, e.getMessage());
            return createFallbackDemandResponse(productId, "Analytics service unavailable");
        }
    }

    /**
     * Get reorder alerts for products running low.
     */
    public List<ReorderAlert> getReorderAlerts(Integer thresholdDays) {
        if (!analyticsEnabled) {
            return Collections.emptyList();
        }

        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl(analyticsServiceUrl + "/internal/demand/alerts/reorder")
                    .queryParamIfPresent("threshold_days", Optional.ofNullable(thresholdDays))
                    .toUriString();

            ResponseEntity<List<ReorderAlert>> response = restTemplate.exchange(
                    url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<ReorderAlert>>() {});

            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (RestClientException e) {
            log.warn("Failed to fetch reorder alerts: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    // ========================================================================
    // BUNDLE INSIGHTS
    // ========================================================================

    /**
     * Get bundle insights (product associations).
     */
    public BundleInsightResponse getBundleInsights(Double minSupport, Double minConfidence, Integer limit) {
        if (!analyticsEnabled) {
            return createFallbackBundleResponse("Analytics service is disabled");
        }

        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl(analyticsServiceUrl + "/internal/bundles")
                    .queryParamIfPresent("min_support", Optional.ofNullable(minSupport))
                    .queryParamIfPresent("min_confidence", Optional.ofNullable(minConfidence))
                    .queryParamIfPresent("limit", Optional.ofNullable(limit))
                    .toUriString();

            ResponseEntity<BundleInsightResponse> response = restTemplate.getForEntity(
                    url, BundleInsightResponse.class);

            return response.getBody();
        } catch (RestClientException e) {
            log.warn("Failed to fetch bundle insights: {}", e.getMessage());
            return createFallbackBundleResponse("Analytics service unavailable");
        }
    }

    /**
     * Get bundle analysis data status.
     */
    public BundleDataStatus getBundleDataStatus() {
        if (!analyticsEnabled) {
            return BundleDataStatus.builder()
                    .readyForAnalysis(false)
                    .message("Analytics service is disabled")
                    .build();
        }

        try {
            String url = analyticsServiceUrl + "/internal/bundles/data-status";
            ResponseEntity<BundleDataStatus> response = restTemplate.getForEntity(
                    url, BundleDataStatus.class);

            return response.getBody();
        } catch (RestClientException e) {
            log.warn("Failed to fetch bundle data status: {}", e.getMessage());
            return BundleDataStatus.builder()
                    .readyForAnalysis(false)
                    .message("Analytics service unavailable: " + e.getMessage())
                    .build();
        }
    }

    // ========================================================================
    // HEALTH CHECK
    // ========================================================================

    /**
     * Check if the analytics service is healthy.
     */
    public boolean isHealthy() {
        if (!analyticsEnabled) {
            return false;
        }

        try {
            String url = analyticsServiceUrl + "/health";
            ResponseEntity<AnalyticsHealthResponse> response = restTemplate.getForEntity(
                    url, AnalyticsHealthResponse.class);

            return response.getStatusCode().is2xxSuccessful() &&
                   response.getBody() != null &&
                   "healthy".equals(response.getBody().getStatus());
        } catch (RestClientException e) {
            log.warn("Analytics service health check failed: {}", e.getMessage());
            return false;
        }
    }

    // ========================================================================
    // FALLBACK RESPONSES
    // ========================================================================

    private MarginInsightResponse createFallbackMarginResponse(String message) {
        return MarginInsightResponse.builder()
                .dataQuality("UNAVAILABLE")
                .summary(MarginSummary.builder()
                        .totalProducts(0)
                        .healthyMargins(0)
                        .thinMargins(0)
                        .negativeMargins(0)
                        .atRiskOfWaste(0)
                        .totalPotentialWasteValue(0)
                        .build())
                .alerts(Collections.emptyList())
                .generatedAt(LocalDateTime.now())
                .build();
    }

    private DemandInsightResponse createFallbackDemandResponse(String productId, String message) {
        return DemandInsightResponse.builder()
                .productId(productId)
                .productName("Unknown")
                .dataQuality("UNAVAILABLE")
                .insightMessage(message)
                .generatedAt(LocalDateTime.now())
                .dataRange(DataRange.builder().daysOfData(0).build())
                .build();
    }

    private BundleInsightResponse createFallbackBundleResponse(String message) {
        return BundleInsightResponse.builder()
                .dataQuality("UNAVAILABLE")
                .totalOrdersAnalyzed(0)
                .multiItemOrdersAnalyzed(0)
                .rules(Collections.emptyList())
                .placementSuggestions(Collections.emptyList())
                .insightMessage(message)
                .generatedAt(LocalDateTime.now())
                .build();
    }
}
