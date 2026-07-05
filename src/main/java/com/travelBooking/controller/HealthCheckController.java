package com.travelBooking.controller;

import com.travelBooking.dto.HealthCheckResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.travelBooking.service.HealthCheckService;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/health")
public class HealthCheckController {

    private static final Logger logger = Logger.getLogger(HealthCheckController.class.getName());

    private final HealthCheckService healthCheckService;

    public HealthCheckController(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
    }

    @GetMapping
    public ResponseEntity<HealthCheckResponse> healthCheck() {
        logger.info("Health check endpoint called");
        
        HealthCheckResponse response = healthCheckService.getHealthStatus();
        HttpStatus status = response.isHealthy() ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
        
        return new ResponseEntity<>(response, status);
    }
}
