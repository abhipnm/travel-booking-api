# Travel Booking Management System - API Documentation

## Overview
Production-ready REST API for travel booking management with H2 in-memory database. Built with Spring Boot 3.1.5 and follows enterprise-level best practices.

**Server:** http://localhost:8081

---

## Endpoints

### 1. Health Check
**GET** `/api/v1/health`
- Check application health status
- Response: 200 OK

```bash
curl http://localhost:8081/api/v1/health
```

Response:
```json
{
    "status": "UP",
    "healthy": true,
    "timestamp": "2026-07-05T09:18:35.356637Z",
    "message": "Application is running normally"
}
```

---

### 2. Create Booking
**POST** `/api/v1/bookings`
- Create a new travel booking
- Response: 201 CREATED

```bash
curl -X POST http://localhost:8081/api/v1/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "John Doe",
    "customerEmail": "john@example.com",
    "destination": "Paris",
    "departureDate": "2026-08-15",
    "returnDate": "2026-08-25",
    "numberOfPeople": 2,
    "totalPrice": 5000.00
  }'
```

**Request Fields:**
- `customerName` (required): String, 2-100 characters
- `customerEmail` (required): Valid email format
- `destination` (required): String, 2-100 characters
- `departureDate` (required): Date in YYYY-MM-DD format, future or present
- `returnDate` (required): Date in YYYY-MM-DD format, future or present
- `numberOfPeople` (required): Integer, 1-20
- `totalPrice` (required): Decimal > 0

Response:
```json
{
    "id": 1,
    "bookingReference": "BK-4E46C915",
    "customerName": "John Doe",
    "customerEmail": "john@example.com",
    "destination": "Paris",
    "departureDate": "2026-08-15",
    "returnDate": "2026-08-25",
    "numberOfPeople": 2,
    "totalPrice": 5000.0,
    "status": "PENDING",
    "createdAt": "2026-07-05T14:48:35.580410",
    "updatedAt": "2026-07-05T14:48:35.580676"
}
```

---

### 3. Get All Bookings
**GET** `/api/v1/bookings`
- Retrieve all bookings or filter by email/status
- Response: 200 OK

```bash
# Get all bookings
curl http://localhost:8081/api/v1/bookings

# Filter by customer email
curl "http://localhost:8081/api/v1/bookings?email=john@example.com"

# Filter by status (PENDING, CONFIRMED, CANCELLED, COMPLETED)
curl "http://localhost:8081/api/v1/bookings?status=CONFIRMED"
```

Response: Array of BookingResponse objects

---

### 4. Get Booking by ID
**GET** `/api/v1/bookings/{id}`
- Retrieve a specific booking by ID
- Response: 200 OK or 404 Not Found

```bash
curl http://localhost:8081/api/v1/bookings/1
```

---

### 5. Get Booking by Reference
**GET** `/api/v1/bookings/reference/{bookingReference}`
- Retrieve a booking by its booking reference code
- Response: 200 OK or 404 Not Found

```bash
curl "http://localhost:8081/api/v1/bookings/reference/BK-4E46C915"
```

---

### 6. Update Booking
**PUT** `/api/v1/bookings/{id}`
- Update booking details (partial updates supported)
- Response: 200 OK or 404 Not Found

```bash
curl -X PUT http://localhost:8081/api/v1/bookings/1 \
  -H "Content-Type: application/json" \
  -d '{
    "numberOfPeople": 3,
    "totalPrice": 6000.00
  }'
```

**Update Fields** (all optional):
- `customerName`: String, 2-100 characters
- `customerEmail`: Valid email format
- `destination`: String, 2-100 characters
- `departureDate`: Date in YYYY-MM-DD format
- `returnDate`: Date in YYYY-MM-DD format
- `numberOfPeople`: Integer, 1-20
- `totalPrice`: Decimal > 0

---

### 7. Confirm Booking
**POST** `/api/v1/bookings/{id}/confirm`
- Change booking status from PENDING to CONFIRMED
- Response: 200 OK or 404 Not Found

```bash
curl -X POST http://localhost:8081/api/v1/bookings/1/confirm
```

Response:
```json
{
    "id": 1,
    "status": "CONFIRMED",
    ...
}
```

---

### 8. Cancel Booking
**POST** `/api/v1/bookings/{id}/cancel`
- Change booking status to CANCELLED
- Response: 200 OK or 404 Not Found

```bash
curl -X POST http://localhost:8081/api/v1/bookings/1/cancel
```

---

### 9. Delete Booking
**DELETE** `/api/v1/bookings/{id}`
- Permanently delete a booking
- Response: 204 No Content or 404 Not Found

```bash
curl -X DELETE http://localhost:8081/api/v1/bookings/1
```

---

## Booking Status
- `PENDING`: Newly created, awaiting confirmation
- `CONFIRMED`: Customer confirmed the booking
- `CANCELLED`: Booking was cancelled
- `COMPLETED`: Trip completed

---

## Error Responses

### 404 Not Found
```json
{
    "timestamp": "2026-07-05T14:48:53.279687",
    "status": 404,
    "error": "Not Found",
    "message": "Booking not found with id: 999",
    "details": null
}
```

### 400 Bad Request (Validation Error)
```json
{
    "timestamp": "2026-07-05T14:48:53.344978",
    "status": 400,
    "error": "Validation Error",
    "message": "Input validation failed",
    "details": [
        "customerEmail: Invalid email format",
        "numberOfPeople: At least 1 person is required"
    ]
}
```

### 500 Internal Server Error
```json
{
    "timestamp": "2026-07-05T14:48:53.400000",
    "status": 500,
    "error": "Internal Server Error",
    "message": "An unexpected error occurred"
}
```

---

## Technical Stack

- **Framework:** Spring Boot 3.1.5
- **Language:** Java 17
- **Database:** H2 (In-memory, auto-created on startup)
- **ORM:** Hibernate/JPA
- **Build Tool:** Maven
- **Server:** Tomcat (embedded)

---

## Database Schema

### Bookings Table
```sql
CREATE TABLE bookings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    booking_reference VARCHAR(255) NOT NULL UNIQUE,
    customer_name VARCHAR(100) NOT NULL,
    customer_email VARCHAR(255) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    departure_date DATE NOT NULL,
    return_date DATE NOT NULL,
    number_of_people INT NOT NULL,
    total_price DOUBLE NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
```

---

## Features

✅ CRUD operations for bookings
✅ Input validation with comprehensive error messages
✅ Search by email and status
✅ Booking status management (confirm, cancel)
✅ Automatic timestamp tracking (created_at, updated_at)
✅ Unique booking reference generation
✅ Global exception handling
✅ H2 in-memory database (perfect for testing)
✅ Comprehensive logging
✅ Production-ready code structure

---

## Running the Application

```bash
# Build
mvn clean package -DskipTests

# Run
java -jar target/travel-booking-api-1.0.0.jar

# Access H2 Console
http://localhost:8081/h2-console
```

**H2 Console Credentials:**
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (empty)

---

## Code Structure

```
src/main/java/com/travelBooking/
├── TravelBookingApplication.java       # Main entry point
├── controller/
│   ├── HealthCheckController.java      # Health check endpoint
│   └── BookingController.java          # Booking CRUD endpoints
├── service/
│   ├── HealthCheckService.java
│   └── BookingService.java             # Business logic
├── entity/
│   ├── Booking.java                    # JPA entity
│   └── BookingStatus.java              # Enum
├── repository/
│   └── BookingRepository.java          # Data access
├── dto/
│   ├── HealthCheckResponse.java
│   ├── CreateBookingRequest.java
│   ├── UpdateBookingRequest.java
│   └── BookingResponse.java
└── exception/
    ├── BookingNotFoundException.java
    ├── ErrorResponse.java
    └── GlobalExceptionHandler.java
```

---

## Production Considerations

✅ Input validation on all endpoints
✅ Structured error responses
✅ Logging for debugging
✅ Separation of concerns (Controller → Service → Repository)
✅ Database transaction management
✅ RESTful API design
✅ API versioning (/api/v1/)
✅ Unique booking reference generation
✅ Timestamp tracking for audit
✅ Status-based operations

---

## Future Enhancements

- Add pagination for booking list
- Add sorting capabilities
- Add authentication/authorization
- Add payment integration
- Add email notifications
- Add data export (CSV, PDF)
- Add advanced search/filtering
- Add database persistence (MySQL, PostgreSQL)
- Add API rate limiting
- Add request/response logging middleware
