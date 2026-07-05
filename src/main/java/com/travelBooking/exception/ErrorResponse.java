package com.travelBooking.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    @JsonProperty("status")
    private int status;

    @JsonProperty("error")
    private String error;

    @JsonProperty("message")
    private String message;

    @JsonProperty("details")
    private List<String> details;

    public ErrorResponse() {}

    public ErrorResponse(LocalDateTime timestamp, int status, String error, String message) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public ErrorResponse(LocalDateTime timestamp, int status, String error, String message, List<String> details) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.details = details;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<String> getDetails() { return details; }
    public void setDetails(List<String> details) { this.details = details; }
}
