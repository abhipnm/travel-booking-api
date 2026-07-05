package com.travelBooking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelBooking.dto.CreateBookingRequest;
import com.travelBooking.entity.BookingStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testHealthCheck() throws Exception {
        mockMvc.perform(get("/api/v1/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.healthy").value(true));
    }

    @Test
    void testCreateBooking_Success() throws Exception {
        CreateBookingRequest request = new CreateBookingRequest(
                "John Doe",
                "john@example.com",
                "Paris",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(10),
                2,
                5000.0
        );

        mockMvc.perform(post("/api/v1/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.bookingReference").exists())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.customerName").value("John Doe"));
    }

    @Test
    void testCreateBooking_InvalidEmail() throws Exception {
        CreateBookingRequest request = new CreateBookingRequest(
                "John Doe",
                "invalid-email",
                "Paris",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(10),
                2,
                5000.0
        );

        mockMvc.perform(post("/api/v1/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"));
    }

    @Test
    void testCreateBooking_InvalidNumberOfPeople() throws Exception {
        CreateBookingRequest request = new CreateBookingRequest(
                "John Doe",
                "john@example.com",
                "Paris",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(10),
                25,
                5000.0
        );

        mockMvc.perform(post("/api/v1/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details[0]").exists());
    }

    @Test
    void testGetAllBookings() throws Exception {
        mockMvc.perform(get("/api/v1/bookings"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetBookingById_NotFound() throws Exception {
        mockMvc.perform(get("/api/v1/bookings/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void testConfirmBooking() throws Exception {
        CreateBookingRequest request = new CreateBookingRequest(
                "Jane Smith",
                "jane@example.com",
                "London",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(10),
                1,
                3000.0
        );

        String response = mockMvc.perform(post("/api/v1/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long bookingId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(post("/api/v1/bookings/{id}/confirm", bookingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void testCancelBooking() throws Exception {
        CreateBookingRequest request = new CreateBookingRequest(
                "Test User",
                "test@example.com",
                "Tokyo",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(10),
                2,
                7000.0
        );

        String response = mockMvc.perform(post("/api/v1/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long bookingId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(post("/api/v1/bookings/{id}/cancel", bookingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    void testDeleteBooking() throws Exception {
        CreateBookingRequest request = new CreateBookingRequest(
                "Delete Test",
                "delete@example.com",
                "Berlin",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(10),
                1,
                2500.0
        );

        String response = mockMvc.perform(post("/api/v1/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long bookingId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/v1/bookings/{id}", bookingId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/bookings/{id}", bookingId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFilterByEmail() throws Exception {
        String email = "filter@example.com";
        
        CreateBookingRequest request = new CreateBookingRequest(
                "Filter Test",
                email,
                "Amsterdam",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(10),
                2,
                4000.0
        );

        mockMvc.perform(post("/api/v1/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/bookings?email=" + email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerEmail").value(email));
    }

    @Test
    void testFilterByStatus() throws Exception {
        CreateBookingRequest request = new CreateBookingRequest(
                "Status Filter",
                "status@example.com",
                "Rome",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(10),
                3,
                6000.0
        );

        mockMvc.perform(post("/api/v1/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/bookings?status=PENDING"))
                .andExpect(status().isOk());
    }
}
