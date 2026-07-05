package com.travelBooking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class HealthCheckResponse {

    @JsonProperty("status")
    private String status;

    @JsonProperty("healthy")
    private boolean healthy;

    @JsonProperty("timestamp")
    private Instant timestamp;

    @JsonProperty("message")
    private String message;

    public HealthCheckResponse() {}

    public HealthCheckResponse(String status, boolean healthy, Instant timestamp, String message) {
        this.status = status;
        this.healthy = healthy;
        this.timestamp = timestamp;
        this.message = message;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isHealthy() { return healthy; }
    public void setHealthy(boolean healthy) { this.healthy = healthy; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String status;
        private boolean healthy;
        private Instant timestamp;
        private String message;

        public Builder status(String status) { this.status = status; return this; }
        public Builder healthy(boolean healthy) { this.healthy = healthy; return this; }
        public Builder timestamp(Instant timestamp) { this.timestamp = timestamp; return this; }
        public Builder message(String message) { this.message = message; return this; }

        public HealthCheckResponse build() {
            return new HealthCheckResponse(status, healthy, timestamp, message);
        }
    }
}
