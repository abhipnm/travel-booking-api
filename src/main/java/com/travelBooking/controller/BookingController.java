package com.travelBooking.controller;

import com.travelBooking.dto.BookingResponse;
import com.travelBooking.dto.CreateBookingRequest;
import com.travelBooking.dto.UpdateBookingRequest;
import com.travelBooking.entity.BookingStatus;
import com.travelBooking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private static final Logger logger = Logger.getLogger(BookingController.class.getName());

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody CreateBookingRequest request) {
        logger.info("POST /api/v1/bookings - Creating new booking");
        BookingResponse response = bookingService.createBooking(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long id) {
        logger.info("GET /api/v1/bookings/{" + id + "} - Fetching booking");
        BookingResponse response = bookingService.getBookingById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/reference/{bookingReference}")
    public ResponseEntity<BookingResponse> getBookingByReference(@PathVariable String bookingReference) {
        logger.info("GET /api/v1/bookings/reference/{" + bookingReference + "} - Fetching booking");
        BookingResponse response = bookingService.getBookingByReference(bookingReference);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllBookings(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) BookingStatus status) {
        
        logger.info("GET /api/v1/bookings - Fetching bookings");
        
        List<BookingResponse> bookings;
        if (email != null) {
            bookings = bookingService.getBookingsByCustomerEmail(email);
        } else if (status != null) {
            bookings = bookingService.getBookingsByStatus(status);
        } else {
            bookings = bookingService.getAllBookings();
        }
        
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingResponse> updateBooking(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBookingRequest request) {
        logger.info("PUT /api/v1/bookings/{" + id + "} - Updating booking");
        BookingResponse response = bookingService.updateBooking(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<BookingResponse> confirmBooking(@PathVariable Long id) {
        logger.info("POST /api/v1/bookings/{" + id + "}/confirm - Confirming booking");
        BookingResponse response = bookingService.confirmBooking(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(@PathVariable Long id) {
        logger.info("POST /api/v1/bookings/{" + id + "}/cancel - Cancelling booking");
        BookingResponse response = bookingService.cancelBooking(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        logger.info("DELETE /api/v1/bookings/{" + id + "} - Deleting booking");
        bookingService.deleteBooking(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
