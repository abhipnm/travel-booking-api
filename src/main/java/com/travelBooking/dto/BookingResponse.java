package com.travelBooking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.travelBooking.entity.BookingStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("bookingReference")
    private String bookingReference;

    @JsonProperty("customerName")
    private String customerName;

    @JsonProperty("customerEmail")
    private String customerEmail;

    @JsonProperty("destination")
    private String destination;

    @JsonProperty("departureDate")
    private LocalDate departureDate;

    @JsonProperty("returnDate")
    private LocalDate returnDate;

    @JsonProperty("numberOfPeople")
    private Integer numberOfPeople;

    @JsonProperty("totalPrice")
    private Double totalPrice;

    @JsonProperty("status")
    private BookingStatus status;

    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;

    public BookingResponse() {}

    public BookingResponse(Long id, String bookingReference, String customerName, String customerEmail,
                           String destination, LocalDate departureDate, LocalDate returnDate,
                           Integer numberOfPeople, Double totalPrice, BookingStatus status,
                           LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.bookingReference = bookingReference;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.destination = destination;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.numberOfPeople = numberOfPeople;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public Long getId() { return id; }
    public String getBookingReference() { return bookingReference; }
    public String getCustomerName() { return customerName; }
    public String getCustomerEmail() { return customerEmail; }
    public String getDestination() { return destination; }
    public LocalDate getDepartureDate() { return departureDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public Integer getNumberOfPeople() { return numberOfPeople; }
    public Double getTotalPrice() { return totalPrice; }
    public BookingStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
