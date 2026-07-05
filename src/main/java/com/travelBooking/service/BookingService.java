package com.travelBooking.service;

import com.travelBooking.dto.BookingResponse;
import com.travelBooking.dto.CreateBookingRequest;
import com.travelBooking.dto.UpdateBookingRequest;
import com.travelBooking.entity.Booking;
import com.travelBooking.entity.BookingStatus;
import com.travelBooking.exception.BookingNotFoundException;
import com.travelBooking.repository.BookingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private static final Logger logger = Logger.getLogger(BookingService.class.getName());

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public BookingResponse createBooking(CreateBookingRequest request) {
        logger.info("Creating new booking for customer: " + request.getCustomerEmail());

        String bookingReference = generateBookingReference();
        Booking booking = new Booking(
                bookingReference,
                request.getCustomerName(),
                request.getCustomerEmail(),
                request.getDestination(),
                request.getDepartureDate(),
                request.getReturnDate(),
                request.getNumberOfPeople(),
                request.getTotalPrice(),
                BookingStatus.PENDING
        );

        Booking savedBooking = bookingRepository.save(booking);
        logger.info("Booking created with reference: " + bookingReference);
        return mapToResponse(savedBooking);
    }

    public BookingResponse getBookingById(Long id) {
        logger.info("Fetching booking with id: " + id);
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id: " + id));
        return mapToResponse(booking);
    }

    public BookingResponse getBookingByReference(String bookingReference) {
        logger.info("Fetching booking with reference: " + bookingReference);
        Booking booking = bookingRepository.findByBookingReference(bookingReference)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with reference: " + bookingReference));
        return mapToResponse(booking);
    }

    public List<BookingResponse> getBookingsByCustomerEmail(String email) {
        logger.info("Fetching bookings for customer: " + email);
        List<Booking> bookings = bookingRepository.findByCustomerEmail(email);
        return bookings.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<BookingResponse> getBookingsByStatus(BookingStatus status) {
        logger.info("Fetching bookings with status: " + status);
        List<Booking> bookings = bookingRepository.findByStatus(status);
        return bookings.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<BookingResponse> getAllBookings() {
        logger.info("Fetching all bookings");
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public BookingResponse updateBooking(Long id, UpdateBookingRequest request) {
        logger.info("Updating booking with id: " + id);
        
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id: " + id));

        if (request.getCustomerName() != null) booking.setCustomerName(request.getCustomerName());
        if (request.getCustomerEmail() != null) booking.setCustomerEmail(request.getCustomerEmail());
        if (request.getDestination() != null) booking.setDestination(request.getDestination());
        if (request.getDepartureDate() != null) booking.setDepartureDate(request.getDepartureDate());
        if (request.getReturnDate() != null) booking.setReturnDate(request.getReturnDate());
        if (request.getNumberOfPeople() != null) booking.setNumberOfPeople(request.getNumberOfPeople());
        if (request.getTotalPrice() != null) booking.setTotalPrice(request.getTotalPrice());

        Booking updatedBooking = bookingRepository.save(booking);
        logger.info("Booking updated successfully");
        return mapToResponse(updatedBooking);
    }

    public BookingResponse confirmBooking(Long id) {
        logger.info("Confirming booking with id: " + id);
        
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id: " + id));
        
        booking.setStatus(BookingStatus.CONFIRMED);
        Booking confirmedBooking = bookingRepository.save(booking);
        logger.info("Booking confirmed with reference: " + booking.getBookingReference());
        return mapToResponse(confirmedBooking);
    }

    public BookingResponse cancelBooking(Long id) {
        logger.info("Cancelling booking with id: " + id);
        
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id: " + id));
        
        booking.setStatus(BookingStatus.CANCELLED);
        Booking cancelledBooking = bookingRepository.save(booking);
        logger.info("Booking cancelled with reference: " + booking.getBookingReference());
        return mapToResponse(cancelledBooking);
    }

    public void deleteBooking(Long id) {
        logger.info("Deleting booking with id: " + id);
        
        if (!bookingRepository.existsById(id)) {
            throw new BookingNotFoundException("Booking not found with id: " + id);
        }
        
        bookingRepository.deleteById(id);
        logger.info("Booking deleted successfully");
    }

    private BookingResponse mapToResponse(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getBookingReference(),
                booking.getCustomerName(),
                booking.getCustomerEmail(),
                booking.getDestination(),
                booking.getDepartureDate(),
                booking.getReturnDate(),
                booking.getNumberOfPeople(),
                booking.getTotalPrice(),
                booking.getStatus(),
                booking.getCreatedAt(),
                booking.getUpdatedAt()
        );
    }

    private String generateBookingReference() {
        return "BK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
