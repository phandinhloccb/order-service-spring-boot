# Order Service - Clean Architecture Implementation

A Spring Boot microservice for order management built following Clean Architecture principles, implementing the Ports & Adapters pattern with proper dependency inversion.

## 📋 Table of Contents

- [Architecture Overview](#architecture-overview)
- [Project Structure](#project-structure)
- [Clean Architecture Layers](#clean-architecture-layers)
- [Key Features](#key-features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Testing Strategy](#testing-strategy)
- [Exception Handling](#exception-handling)
- [Event-Driven Architecture](#event-driven-architecture)

## 🏗️ Architecture Overview

This project implements Clean Architecture with the following core principles:

- **Dependency Inversion**: Inner layers define interfaces, outer layers implement them
- **Separation of Concerns**: Each layer has a single responsibility
- **Framework Independence**: Business logic is isolated from frameworks
- **Testability**: Easy to unit test with proper mocking

### Architecture Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                        Controllers                          │
│              (HTTP/REST API Interface)                      │
└─────────────────────┬───────────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────────┐
│                   Application                               │
│           (Use Cases & Business Rules)                      │
└─────────────────────┬───────────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────────┐
│                     Domain                                  │
│              (Entities & Business Logic)                    │
└─────────────────────┬───────────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────────┐
│                 Infrastructure                              │
│        (Database, External APIs, Messaging)                │
└─────────────────────────────────────────────────────────────┘
```

## 📁 Project Structure

```
src/main/kotlin/com/loc/order_service/
├── domain/                          # Enterprise Business Rules
│   ├── model/
│   │   ├── Order.kt                # Domain Entity
│   │   ├── OrderResult.kt          # Domain Value Object
│   │   └── InventoryResult.kt      # Domain Value Object
│   └── enum/
│       └── OrderStatusEnum.kt      # Domain Enum
│
├── application/                     # Application Business Rules
│   ├── service/
│   │   └── OrderService.kt         # Use Case Implementation
│   └── port/
│       └── repository/
│           └── OrderDomainRepository.kt  # Repository Contract
│
├── infrastructure/                  # Frameworks & Drivers
│   ├── adapter/
│   │   └── OrderRepositoryAdapter.kt    # Repository Implementation
│   ├── entity/
│   │   └── OrderEntity.kt          # JPA Entity
│   ├── repository/
│   │   └── OrderRepository.kt      # JPA Repository Interface
│   ├── mapper/
│   │   └── OrderEntityMapper.kt    # Domain ↔ Entity Mapping
│   ├── producer/
│   │   └── KafkaEventProducer.kt   # Event Publishing
│   └── configuration/
│       ├── InventoryServiceConfig.kt
│       └── kafka/
│           └── KafkaConfiguration.kt
│
└── controller/                      # Interface Adapters
    ├── OrderController.kt          # REST Controller
    └── mapper/
        └── OrderDtoMapper.kt       # DTO ↔ Domain Mapping
```

## 🎯 Clean Architecture Layers

### 1. Domain Layer (Innermost)
- **Purpose**: Contains enterprise business rules and entities
- **Dependencies**: None (pure business logic)
- **Components**:
  - `Order`: Core domain entity representing an order
  - `OrderResult`: Value object for operation results
  - `OrderStatusEnum`: Domain-specific enumerations

### 2. Application Layer
- **Purpose**: Contains application-specific business rules (use cases)
- **Dependencies**: Only depends on Domain layer
- **Components**:
  - `OrderService`: Orchestrates business operations
  - `OrderDomainRepository`: Port (interface) for data access

### 3. Infrastructure Layer (Outermost)
- **Purpose**: Contains frameworks, databases, external services
- **Dependencies**: Implements interfaces from inner layers
- **Components**:
  - `OrderRepositoryAdapter`: Implements domain repository contract
  - `OrderEntity`: JPA entity for database persistence
  - `OrderRepository`: Spring Data JPA repository
  - `KafkaEventProducer`: Event publishing mechanism

### 4. Controller Layer (Interface Adapters)
- **Purpose**: Handles HTTP requests and responses
- **Dependencies**: Uses Application layer services
- **Components**:
  - `OrderController`: REST API endpoints
  - `OrderDtoMapper`: Maps between DTOs and domain models

## ✨ Key Features

- **Clean Architecture Implementation**: Proper dependency inversion and layer separation
- **Event-Driven Architecture**: Kafka integration for order completion events
- **External Service Integration**: Inventory service client with circuit breaker pattern
- **Comprehensive Exception Handling**: Layered exception handling with global handler
- **Database Integration**: JPA/Hibernate with Liquibase migrations
- **API Documentation**: OpenAPI 3.0 specification
- **Comprehensive Testing**: Unit and integration tests for all layers

## 🛠️ Technologies Used

- **Framework**: Spring Boot 3.x
- **Language**: Kotlin
- **Database**: MySQL with JPA/Hibernate
- **Migration**: Liquibase
- **Messaging**: Apache Kafka
- **Documentation**: OpenAPI 3.0 (Swagger)
- **Testing**: JUnit 5, MockK, TestContainers
- **Build Tool**: Gradle with Kotlin DSL

## 🚀 Getting Started

### Prerequisites

- JDK 17 or higher
- Docker and Docker Compose
- Gradle 7.x or higher

### Running the Application

1. **Start Infrastructure Services**:
   ```bash
   cd server
   docker-compose up -d
   ```

2. **Run the Application**:
   ```bash
   ./gradlew bootRun
   ```

3. **Access the API**:
   - Application: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - API Docs: http://localhost:8080/v3/api-docs

### Configuration

The application uses `application.yml` for configuration:

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/order_service
    username: root
    password: password
  
  kafka:
    bootstrap-servers: localhost:9092
    
inventory-service:
  base-url: http://localhost:8081
```

## 📚 API Documentation

### Order Management Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/orders` | Create a new order |
| GET | `/orders/{id}` | Get order by ID |
| GET | `/orders` | Get all orders |
| PUT | `/orders/{id}` | Update order status |

### Request/Response Examples

**Create Order**:
```json
POST /orders
{
  "productId": "12345",
  "quantity": 2,
  "customerId": "customer-1"
}
```

**Response**:
```json
{
  "id": "order-123",
  "productId": "12345",
  "quantity": 2,
  "customerId": "customer-1",
  "status": "PENDING",
  "createdAt": "2024-01-15T10:30:00Z"
}
```

## 🧪 Testing Strategy

### Test Structure Following Clean Architecture

```
src/test/kotlin/com/loc/order_service/
├── application/service/
│   └── OrderServiceTest.kt           # Use Case Tests
├── controller/
│   ├── OrderControllerTest.kt        # API Integration Tests
│   └── mapper/
│       └── OrderDtoMapperTest.kt     # DTO Mapping Tests
└── infrastructure/
    ├── adapter/
    │   └── OrderRepositoryAdapterTest.kt  # Repository Tests
    └── mapper/
        └── OrderEntityMapperTest.kt       # Entity Mapping Tests
```

### Running Tests

```bash
# Run all tests
./gradlew test

# Run specific test categories
./gradlew test --tests "*Controller*"
./gradlew test --tests "*Service*"
./gradlew test --tests "*Repository*"
```

## 🚨 Exception Handling

The application implements a layered exception handling strategy:

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Controller    │    │   Service Layer  │    │ External APIs   │
│                 │    │                  │    │                 │
│ HTTP Requests   │───▶│ Business Logic   │───▶│ Inventory API   │
└─────────────────┘    └──────────────────┘    └─────────────────┘
         │                       │                      │
         │              ┌────────▼────────┐             │
         │              │ Domain          │             │
         │              │ Exceptions      │             │
         │              │ • Business      │             │
         │              │ • Validation    │             │
         │              └─────────────────┘             │
         │                                              │
         │              ┌─────────────────┐             │
         │              │ Infrastructure  │◀────────────┘
         │              │ Exceptions      │
         │              │ • Network       │
         │              │ • Timeout       │
         │              │ • External APIs │
         │              └─────────────────┘
         │                       │
         ▼                       ▼
┌─────────────────────────────────────────┐
│        Global Exception Handler         │
│                                         │
│ • Catch all exceptions                  │
│ • Map to HTTP status codes             │
│ • Create standardized error responses  │
│ • Log errors for monitoring            │
└─────────────────────────────────────────┘
         │
         ▼
┌─────────────────┐
│   HTTP Client   │
│                 │
│ Standardized    │
│ Error Response  │
└─────────────────┘
```

### Exception Types

- **Domain Exceptions**: Business rule violations
- **Infrastructure Exceptions**: External service failures, database errors
- **Global Exception Handler**: Centralized error handling and response formatting

## 📡 Event-Driven Architecture

The service publishes events using Kafka when orders are completed:

```kotlin
// Event Publishing
@Component
class KafkaEventProducer {
    fun publishOrderCompletedEvent(order: Order) {
        val event = OrderCompletedEvent.newBuilder()
            .setOrderId(order.id)
            .setCustomerId(order.customerId)
            .setProductId(order.productId)
            .setQuantity(order.quantity)
            .setTimestamp(Instant.now())
            .build()
        
        kafkaTemplate.send("order-completed", event)
    }
}
```

## 🔄 Clean Architecture Benefits Achieved

1. **Independence**: Business logic is independent of frameworks, UI, and external agencies
2. **Testability**: Easy to test business rules without UI, database, or external services
3. **Flexibility**: Easy to change frameworks, databases, or external services
4. **Maintainability**: Clear separation of concerns makes the code easier to understand and modify
5. **Scalability**: Architecture supports scaling individual components independently

