package com.travelBooking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthCheckResponse {

    @JsonProperty("status")
    private String status;

    @JsonProperty("healthy")
    private boolean healthy;

    @JsonProperty("timestamp")
    private Instant timestamp;

    @JsonProperty("message")
    private String message;
}
