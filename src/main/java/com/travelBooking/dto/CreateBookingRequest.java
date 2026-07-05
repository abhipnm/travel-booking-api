package com.travelBooking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class CreateBookingRequest {

    @JsonProperty("customerName")
    @NotBlank(message = "Customer name is required")
    @Size(min = 2, max = 100, message = "Customer name must be between 2 and 100 characters")
    private String customerName;

    @JsonProperty("customerEmail")
    @NotBlank(message = "Customer email is required")
    @Email(message = "Invalid email format")
    private String customerEmail;

    @JsonProperty("destination")
    @NotBlank(message = "Destination is required")
    @Size(min = 2, max = 100, message = "Destination must be between 2 and 100 characters")
    private String destination;

    @JsonProperty("departureDate")
    @NotNull(message = "Departure date is required")
    @FutureOrPresent(message = "Departure date must be today or in the future")
    private LocalDate departureDate;

    @JsonProperty("returnDate")
    @NotNull(message = "Return date is required")
    @FutureOrPresent(message = "Return date must be today or in the future")
    private LocalDate returnDate;

    @JsonProperty("numberOfPeople")
    @NotNull(message = "Number of people is required")
    @Min(value = 1, message = "At least 1 person is required")
    @Max(value = 20, message = "Maximum 20 people per booking")
    private Integer numberOfPeople;

    @JsonProperty("totalPrice")
    @NotNull(message = "Total price is required")
    @DecimalMin(value = "0.01", message = "Total price must be greater than 0")
    private Double totalPrice;

    public CreateBookingRequest() {}

    public CreateBookingRequest(String customerName, String customerEmail, String destination,
                                LocalDate departureDate, LocalDate returnDate,
                                Integer numberOfPeople, Double totalPrice) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.destination = destination;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.numberOfPeople = numberOfPeople;
        this.totalPrice = totalPrice;
    }

    // Getters and Setters
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public LocalDate getDepartureDate() { return departureDate; }
    public void setDepartureDate(LocalDate departureDate) { this.departureDate = departureDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public Integer getNumberOfPeople() { return numberOfPeople; }
    public void setNumberOfPeople(Integer numberOfPeople) { this.numberOfPeople = numberOfPeople; }

    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }
}
