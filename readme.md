# Ecommerce Spring Boot Project

## Overview
This project is a full-featured Ecommerce backend built with Spring Boot.  
It supports JWT-based authentication including **access tokens**, **refresh tokens with rotation**, and **fine-grained role and permission-based authorization**.  
Redis Cloud is used for caching.

---

## Features
- User registration and login with JWT authentication
- Access and refresh tokens with automatic rotation and revocation
- Role-based and permission-based authorization for secure API access
- Ownership checks to restrict users to their own data
- Integration with Redis Cloud for caching and session storage
- CRUD operations for products, categories, customers, carts, and orders
- Global exception handling and validation with meaningful API error messages
- Implemented pagination for Customer and Product endpoints

---

## Technologies Used
- Java 21
- Spring Boot 3.5.x
- Spring Security with JWT
- Spring Data JPA with PostgreSQL
- Spring Data Redis
- Lombok for boilerplate reduction
- Maven Build Tool

---

## Getting Started

### Prerequisites
- Java JDK 21 or later
- PostgreSQL database setup
- Docker for Redis Server
- Maven 3.x or IntelliJ with Maven support

### Configuration

Create an `application.yml` or `application.properties` file and configure:

```
spring:
  application:
    name: Ecommerce
  datasource:
    url: ${DATABASE_URL}
    username: ${DATABASE_USERNAME}
    password: ${DATABASE_PASSWORD}
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
      show-sql: true
    properties:
      hibernate:
        format-sql: true
  data:
    redis:
      host: localhost
      port: 6379
  cache:
    type: redis

jwt:
  secretKey: ${JWT_SECRET}
  accessExpirationMs: 120000            # 2 MINUTES
  refreshExpirationMs: 300000          # 5 MINUTES
#  sql:
#    init:
#      mode: always
```
---

### Build and Run

``
mvn clean install
mvn spring-boot:run
``
---

---

## API Endpoints

| Endpoint                       | Description                     | Auth Required       |
|--------------------------------|---------------------------------|---------------------|
| POST `/api/auth/register`      | User registration               | No                  |
| POST `/api/auth/login`         | User login - returns JWT tokens | No                  |
| POST `/api/auth/token/refresh` | Refresh access token            | Yes (refresh token) |
| POST `/api/auth/logout`        | Logout and revoke refresh token | Yes                 |
| GET `/api/customers/{id}`      | Get customer info (owner only)  | Yes                 |
| PUT `/api/customers/{id}`      | Update customer (owner only)    | Yes                 |
| GET `api/customers/all`          | Get All Customers (ADMIN only)  | Yes                 |
| ...                            | ...                             | ...                 |

---

## Security Notes
- Access tokens are short-lived JWTs containing user permissions.
- Refresh tokens are stored securely in the database with expiration.
- Token rotation is implemented to mitigate replay attacks.
- Role and permission checks restrict API access with Spring Security method-level guards.
- Custom ownership checks ensure users can only access their own data.

---


## Acknowledgments
- Thanks to the Spring Boot and Redis communities for their great tools and support.



