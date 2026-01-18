package com.fivetpromart.infrastructure.scheduler;

import com.fivetpromart.application.usecase.StockReservationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Background job to auto-expire stock reservations
 * Runs every minute to release expired reservations back to available stock
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class StockReservationExpiryJob {
    
    private final StockReservationUseCase stockReservationUseCase;
    
    /**
     * Runs every 1 minute
     * Vietnam retail pattern: 15-minute reservation window with 1-minute cleanup cycle
     */
    @Scheduled(fixedRate = 60000) // 60,000 ms = 1 minute
    public void expireOldReservations() {
        log.debug("Running stock reservation expiry job");
        
        try {
            stockReservationUseCase.expireOldReservations();
        } catch (Exception e) {
            log.error("Error in stock reservation expiry job", e);
        }
    }
}
