package com.fivetpromart.infrastructure.scheduler;

import com.fivetpromart.application.usecase.StockInventoryUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Background job to mark expired lots as EXPIRED.
 * Runs daily at 02:00 to minimize impact on daytime operations.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class StockInventoryExpiryJob {

    private final StockInventoryUseCase stockInventoryUseCase;

    /**
     * Run daily at 02:00 AM server time
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void run() {
        log.info("Running stock inventory expiry job");
        try {
            int updated = stockInventoryUseCase.markExpiredLots();
            log.info("Stock inventory expiry job completed: {} lots marked EXPIRED", updated);
        } catch (Exception e) {
            log.error("Error in stock inventory expiry job", e);
        }
    }
}
