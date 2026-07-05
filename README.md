# Travel Booking Management System

A production-ready REST API for travel booking management built with Spring Boot 3.1.5, Java 17, and H2 in-memory database.

## 🚀 Features

- **Complete CRUD Operations** - Create, retrieve, update, and delete bookings
- **Advanced Filtering** - Search by customer email or booking status
- **Status Management** - Track booking lifecycle (PENDING → CONFIRMED → CANCELLED/COMPLETED)
- **Input Validation** - Comprehensive validation with detailed error messages
- **Health Monitoring** - Built-in health check endpoint
- **RESTful API** - Versioned endpoints (`/api/v1/`) following REST standards
- **Global Exception Handling** - Centralized error response formatting
- **Comprehensive Logging** - Debug-ready with structured logs
- **H2 Database** - In-memory testing database, easily switch to MySQL/PostgreSQL
- **API Documentation** - Complete endpoint documentation included

## 📋 Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+

### Installation & Running

```bash
# Clone the repository
git clone <repo-url>
cd untitled

# Build the project
mvn clean package -DskipTests

# Run the application
java -jar target/travel-booking-api-1.0.0.jar

# Or use Maven directly
mvn spring-boot:run
```

The application will start on `http://localhost:8081`

## 📚 API Endpoints

### Health Check
```bash
GET /api/v1/health
```

### Booking Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| **POST** | `/api/v1/bookings` | Create a new booking |
| **GET** | `/api/v1/bookings` | Get all bookings (with filters) |
| **GET** | `/api/v1/bookings/{id}` | Get booking by ID |
| **GET** | `/api/v1/bookings/reference/{ref}` | Get booking by reference |
| **PUT** | `/api/v1/bookings/{id}` | Update booking |
| **POST** | `/api/v1/bookings/{id}/confirm` | Confirm booking |
| **POST** | `/api/v1/bookings/{id}/cancel` | Cancel booking |
| **DELETE** | `/api/v1/bookings/{id}` | Delete booking |

### Example: Create Booking

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

### Example Response

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

## 🔍 Query Parameters

### Get All Bookings with Filters

```bash
# Filter by customer email
curl "http://localhost:8081/api/v1/bookings?email=john@example.com"

# Filter by status (PENDING, CONFIRMED, CANCELLED, COMPLETED)
curl "http://localhost:8081/api/v1/bookings?status=CONFIRMED"

# Get all bookings
curl "http://localhost:8081/api/v1/bookings"
```

## 📊 Booking Status Lifecycle

- **PENDING** - Newly created booking, awaiting confirmation
- **CONFIRMED** - Customer has confirmed the booking
- **CANCELLED** - Booking was cancelled by customer or system
- **COMPLETED** - Travel has been completed

## ✅ Input Validation

All requests are validated with the following rules:

| Field | Validation |
|-------|-----------|
| `customerName` | Required, 2-100 characters |
| `customerEmail` | Required, valid email format |
| `destination` | Required, 2-100 characters |
| `departureDate` | Required, must be today or future |
| `returnDate` | Required, must be today or future |
| `numberOfPeople` | Required, 1-20 people |
| `totalPrice` | Required, must be > 0 |

## 🏗️ Project Structure

```
src/main/java/com/travelBooking/
├── TravelBookingApplication.java              # Application entry point
├── controller/
│   ├── HealthCheckController.java            # Health check endpoint
│   └── BookingController.java                # Booking API endpoints
├── service/
│   ├── HealthCheckService.java
│   └── BookingService.java                   # Business logic
├── entity/
│   ├── Booking.java                          # JPA entity
│   └── BookingStatus.java                    # Enum
├── repository/
│   └── BookingRepository.java                # Data access layer
├── dto/
│   ├── HealthCheckResponse.java
│   ├── CreateBookingRequest.java
│   ├── UpdateBookingRequest.java
│   └── BookingResponse.java
└── exception/
    ├── BookingNotFoundException.java
    ├── ErrorResponse.java
    └── GlobalExceptionHandler.java

src/main/resources/
└── application.properties                    # Configuration

pom.xml                                        # Maven dependencies
```

## 🛠️ Technology Stack

| Component | Technology |
|-----------|-----------|
| Language | Java 17 |
| Framework | Spring Boot 3.1.5 |
| Database | H2 (In-Memory) |
| ORM | Hibernate/JPA |
| Build Tool | Maven |
| Server | Apache Tomcat (Embedded) |
| Port | 8081 |

## 📦 Dependencies

- Spring Boot Web Starter
- Spring Boot Data JPA
- Spring Boot Validation
- H2 Database
- Spring Boot Test

## 🔄 Switching to Production Database

To use MySQL or PostgreSQL:

### 1. Add dependency to `pom.xml`:
```xml
<!-- For MySQL -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>

<!-- For PostgreSQL -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

### 2. Update `application.properties`:
```properties
# MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/travel_booking
spring.datasource.username=root
spring.datasource.password=password

# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/travel_booking
spring.datasource.username=postgres
spring.datasource.password=password

spring.jpa.hibernate.ddl-auto=update
```

### 3. Rebuild and restart:
```bash
mvn clean package -DskipTests
java -jar target/travel-booking-api-1.0.0.jar
```

## 🧪 Testing the API

### Using curl

```bash
# Health check
curl http://localhost:8081/api/v1/health

# Create booking
curl -X POST http://localhost:8081/api/v1/bookings \
  -H "Content-Type: application/json" \
  -d '{"customerName":"John","customerEmail":"john@test.com","destination":"Paris","departureDate":"2026-08-15","returnDate":"2026-08-25","numberOfPeople":2,"totalPrice":5000}'

# Get all bookings
curl http://localhost:8081/api/v1/bookings

# Get specific booking
curl http://localhost:8081/api/v1/bookings/1

# Update booking
curl -X PUT http://localhost:8081/api/v1/bookings/1 \
  -H "Content-Type: application/json" \
  -d '{"numberOfPeople":3}'

# Confirm booking
curl -X POST http://localhost:8081/api/v1/bookings/1/confirm

# Cancel booking
curl -X POST http://localhost:8081/api/v1/bookings/1/cancel

# Delete booking
curl -X DELETE http://localhost:8081/api/v1/bookings/1
```

## 📖 Full API Documentation

See [API_DOCUMENTATION.md](API_DOCUMENTATION.md) for comprehensive endpoint documentation.

## 🗄️ H2 Console

Access H2 database console at: `http://localhost:8081/h2-console`

**Credentials:**
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (leave empty)

## 🔐 Error Handling

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

## 📝 Logging

Logs are configured with the following levels:
- **ROOT**: INFO
- **com.travelBooking**: DEBUG (development) / INFO (production)

Check logs in the console or file system for debugging.

## 🚀 Production Deployment

### Build for Production
```bash
mvn clean package -DskipTests -P production
```

### Docker Deployment
Create a `Dockerfile`:
```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/travel-booking-api-1.0.0.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build and run:
```bash
docker build -t travel-booking-api .
docker run -p 8081:8081 travel-booking-api
```

## 📋 Features Roadmap

- [ ] Add pagination for booking lists
- [ ] Add advanced search and filtering
- [ ] Add JWT authentication and authorization
- [ ] Add payment integration
- [ ] Add email notifications
- [ ] Add booking export (CSV, PDF)
- [ ] Add API rate limiting
- [ ] Add request/response logging middleware
- [ ] Add audit trail
- [ ] Add database migrations (Flyway)

## 📄 License

This project is licensed under the MIT License.

## 👤 Author

Created by Abhishek Sharma

## 🤝 Contributing

Contributions are welcome! Please feel free to submit pull requests.

---

**Last Updated:** July 5, 2026
**Version:** 1.0.0
