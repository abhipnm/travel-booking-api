package com.travelBooking.service;

import com.travelBooking.dto.HealthCheckResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.logging.Logger;

@Service
public class HealthCheckService {

    private static final Logger logger = Logger.getLogger(HealthCheckService.class.getName());

    public HealthCheckResponse getHealthStatus() {
        logger.info("Evaluating health status");
        
        boolean isHealthy = checkApplicationHealth();
        
        return HealthCheckResponse.builder()
                .status(isHealthy ? "UP" : "DOWN")
                .healthy(isHealthy)
                .timestamp(Instant.now())
                .message(isHealthy ? "Application is running normally" : "Application health check failed")
                .build();
    }

    private boolean checkApplicationHealth() {
        try {
            return true;
        } catch (Exception e) {
            logger.severe("Health check failed: " + e.getMessage());
            return false;
        }
    }
}
