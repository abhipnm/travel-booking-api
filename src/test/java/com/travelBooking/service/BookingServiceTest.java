package com.travelBooking.service;

import com.travelBooking.dto.BookingResponse;
import com.travelBooking.dto.CreateBookingRequest;
import com.travelBooking.entity.Booking;
import com.travelBooking.entity.BookingStatus;
import com.travelBooking.exception.BookingNotFoundException;
import com.travelBooking.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookingServiceTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
    }

    @Test
    void testCreateBooking() {
        CreateBookingRequest request = new CreateBookingRequest(
                "John Doe",
                "john@test.com",
                "Paris",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(10),
                2,
                5000.0
        );

        BookingResponse response = bookingService.createBooking(request);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertNotNull(response.getBookingReference());
        assertEquals("John Doe", response.getCustomerName());
        assertEquals("john@test.com", response.getCustomerEmail());
        assertEquals(BookingStatus.PENDING, response.getStatus());
    }

    @Test
    void testGetBookingById() {
        CreateBookingRequest request = new CreateBookingRequest(
                "Jane Smith",
                "jane@test.com",
                "London",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(10),
                1,
                3000.0
        );

        BookingResponse created = bookingService.createBooking(request);
        BookingResponse retrieved = bookingService.getBookingById(created.getId());

        assertNotNull(retrieved);
        assertEquals(created.getId(), retrieved.getId());
        assertEquals("Jane Smith", retrieved.getCustomerName());
    }

    @Test
    void testGetBookingById_NotFound() {
        assertThrows(BookingNotFoundException.class, () -> {
            bookingService.getBookingById(999L);
        });
    }

    @Test
    void testConfirmBooking() {
        CreateBookingRequest request = new CreateBookingRequest(
                "Test User",
                "test@test.com",
                "Tokyo",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(10),
                2,
                7000.0
        );

        BookingResponse created = bookingService.createBooking(request);
        assertEquals(BookingStatus.PENDING, created.getStatus());

        BookingResponse confirmed = bookingService.confirmBooking(created.getId());
        assertEquals(BookingStatus.CONFIRMED, confirmed.getStatus());
    }

    @Test
    void testCancelBooking() {
        CreateBookingRequest request = new CreateBookingRequest(
                "Cancel Test",
                "cancel@test.com",
                "Berlin",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(10),
                1,
                2500.0
        );

        BookingResponse created = bookingService.createBooking(request);
        BookingResponse cancelled = bookingService.cancelBooking(created.getId());

        assertEquals(BookingStatus.CANCELLED, cancelled.getStatus());
    }

    @Test
    void testDeleteBooking() {
        CreateBookingRequest request = new CreateBookingRequest(
                "Delete Test",
                "delete@test.com",
                "Amsterdam",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(10),
                3,
                4000.0
        );

        BookingResponse created = bookingService.createBooking(request);
        Long bookingId = created.getId();

        bookingService.deleteBooking(bookingId);

        assertThrows(BookingNotFoundException.class, () -> {
            bookingService.getBookingById(bookingId);
        });
    }

    @Test
    void testGetBookingsByEmail() {
        String email = "email@test.com";
        CreateBookingRequest request = new CreateBookingRequest(
                "Email Filter",
                email,
                "Rome",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(10),
                2,
                6000.0
        );

        bookingService.createBooking(request);
        var bookings = bookingService.getBookingsByCustomerEmail(email);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(email, bookings.get(0).getCustomerEmail());
    }

    @Test
    void testGetBookingsByStatus() {
        CreateBookingRequest request = new CreateBookingRequest(
                "Status Test",
                "status@test.com",
                "Madrid",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(10),
                1,
                3500.0
        );

        bookingService.createBooking(request);
        var pendingBookings = bookingService.getBookingsByStatus(BookingStatus.PENDING);

        assertTrue(pendingBookings.size() > 0);
        assertTrue(pendingBookings.stream().allMatch(b -> b.getStatus() == BookingStatus.PENDING));
    }

    @Test
    void testUpdateBooking() {
        CreateBookingRequest request = new CreateBookingRequest(
                "Update Test",
                "update@test.com",
                "Barcelona",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(10),
                2,
                5000.0
        );

        BookingResponse created = bookingService.createBooking(request);

        com.travelBooking.dto.UpdateBookingRequest updateRequest = new com.travelBooking.dto.UpdateBookingRequest();
        updateRequest.setNumberOfPeople(4);
        updateRequest.setTotalPrice(8000.0);

        BookingResponse updated = bookingService.updateBooking(created.getId(), updateRequest);

        assertEquals(4, updated.getNumberOfPeople());
        assertEquals(8000.0, updated.getTotalPrice());
    }
}
