package com.travelBooking.repository;

import com.travelBooking.entity.Booking;
import com.travelBooking.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByBookingReference(String bookingReference);
    List<Booking> findByCustomerEmail(String customerEmail);
    List<Booking> findByStatus(BookingStatus status);
}
