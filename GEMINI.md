# GEMINI Document

## Project Overview

This project is the back-end for a comprehensive restaurant management system. It provides a RESTful API to manage various aspects of a restaurant, including food, orders, billing, and user accounts. The system is built with Java and the Spring Boot framework, using PostgreSQL as its database. It also integrates modern features like real-time chat, a chatbot for customer interaction, and secure authentication using JWT.

The front-end for this application is developed separately and can be found at: [RestaurantFrontendV2](https://github.com/trandhung260704/RestaurantFrontendV2.git)

## Key Technologies & Tools

*   **Backend:** Java 21, Spring Boot 3.5.3
*   **Database:** PostgreSQL
*   **Database Migration:** Liquibase
*   **Authentication:** Spring Security, JSON Web Token (JWT)
*   **Real-time Communication:** WebSockets
*   **AI & Machine Learning:**
    *   OpenAI GPT-3.5-Turbo for chatbot functionality.
    *   Langchain4j for building AI-powered features.
    *   Qdrant & Chroma for vector database and search capabilities.
*   **Deployment:** Docker, Docker Compose
*   **Dependencies:**
    *   `spring-boot-starter-web`: For building RESTful APIs.
    *   `spring-boot-starter-data-jpa`: For data persistence with JPA and Hibernate.
    *   `spring-boot-starter-security`: For handling security and authentication.
    *   `jjwt`: For creating and parsing JSON Web Tokens.
    *   `langchain4j`: For integrating with large language models.
    *   `postgresql`: JDBC driver for PostgreSQL.
    *   `lombok`: To reduce boilerplate code.

## Project Structure

The project follows a standard Maven directory layout. Key directories and files include:

```
.
├── build.gradle               # Project dependencies and build configuration
├── Dockerfile                 # Instructions to build the Docker image for the application
├── docker-compose.yml         # Defines services, networks, and volumes for Docker
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com/example/demo
│   │   │       ├── config       # Spring Security, CORS, WebSocket configurations
│   │   │       ├── controller   # REST API endpoints
│   │   │       ├── dto          # Data Transfer Objects
│   │   │       ├── entity       # JPA entities representing database tables
│   │   │       ├── jwt          # JWT generation and validation utilities
│   │   │       ├── repository   # Spring Data JPA repositories
│   │   │       └── service      # Business logic and services
│   │   └── resources
│   │       ├── application.properties # Application configuration
│   │       └── db/changelog       # Liquibase database migration scripts
│   └── test                     # Test source code
└── README.md                    # Project README file
```

## Core Functionalities

The application provides the following core features through its API:

*   **User Management:**
    *   User registration and login (`/api/users`, `/api/login`).
    *   Google login integration (`/api/login-google`).
    *   Role-based access control (Admin, Moderator, User).
*   **Food & Menu:**
    *   CRUD operations for food items (`/api/foods`).
    *   Image upload for food items.
    *   Semantic search for food items using AI (`/api/search/food`).
*   **Order Management:**
    *   Create and view orders (`/api/orders`).
*   **Billing & Discounts:**
    *   Manage bills (`/api/bills`).
    *   Manage discount codes (`/api/discounts`).
*   **Inventory:**
    *   Manage ingredients (`/api/ingredients`).
*   **Restaurant Information:**
    *   View and update restaurant details (`/api/restaurant-info`).
*   **Chat & Support:**
    *   Real-time chat between users (`/ws`).
    *   AI-powered chatbot to answer customer queries (`/api/chatbot/chat`).
*   **Analytics:**
    *   Get summary statistics for the restaurant (`/api/statistics/summary`).

## Configuration

Key configuration details are located in `src/main/resources/application.properties`:

*   **Database:**
    *   `spring.datasource.url`: `jdbc:postgresql://localhost:5432/Restaurant_version2`
    *   `spring.datasource.username`: `postgres`
    *   `spring.datasource.password`: `2607`
*   **Server Port:**
    *   `server.port`: `8099`
*   **OpenAI API:**
    *   `openai.api.key`: (Requires a valid OpenAI API key)
*   **Qdrant (Vector Database):**
    *   `qdrant.host`: `localhost`
    *   `qdrant.port`: `6333`

## How to Run

The application is designed to be run with Docker.

1.  **Prerequisites:**
    *   Docker and Docker Compose must be installed.
    *   An OpenAI API key must be set in `application.properties`.

2.  **Build and Run:**
    *   Navigate to the project's root directory.
    *   Run the command: `docker-compose up --build`
    *   This will build the Spring Boot application, create a Docker image, and start the application container along with a PostgreSQL database container.

3.  **Accessing the Application:**
    *   The API will be available at `http://localhost:8099`.

## File Naming Conventions

The project follows standard Java and Spring Boot naming conventions:

*   **Java Classes:** `PascalCase` (e.g., `BillController`, `FoodService`).
*   **Packages:** `lowercase` (e.g., `com.example.demo.controller`).
*   **Methods:** `camelCase` (e.g., `getAllBills`, `createFood`).
*   **Constants:** `UPPER_SNAKE_CASE` (e.g., `Default_Role`).
*   **Configuration Files:** `application.properties`, `docker-compose.yml`.
*   **Database Scripts:** `kebab-case` (e.g., `changeset-01-init.xml`).

## User Roles & Permissions

The application defines three user roles:

*   `ROLE_ADMIN`: Administrator with full access.
*   `ROLE_MODERATOR`: Moderator with elevated privileges.
*   `ROLE_USER`: Standard user with basic access.

The current security configuration (`SecurityConfig.java`) distinguishes between public and authenticated routes:

*   **Public Routes:**
    *   `/api/login`: User login.
    *   `/api/login-google`: Google Sign-In.
    *   `/ws/**`: WebSocket endpoint for real-time chat.
*   **Authenticated Routes:**
    *   All other API routes require a valid JWT token.

*Note: The application currently does not implement fine-grained, role-based restrictions on specific endpoints within the controller layer. All authenticated users can access all non-public endpoints.*

## API Endpoint Guide

Below is a summary of the available API endpoints for interacting with the application.

### Authentication
*   `POST /api/login`: Authenticates a user with username and password, and returns a JWT token.
*   `POST /api/login-google`: Authenticates a user with a Google account.

### Food Management
*   `GET /api/foods`: Retrieves a list of all food items.
*   `GET /api/foods/{id}`: Retrieves a specific food item by its ID.
*   `POST /api/foods`: Creates a new food item. Requires a multipart request with `food` (JSON) and `image` (file) parts.
*   `PUT /api/foods/{id}`: Updates an existing food item.
*   `DELETE /api/foods/{id}`: Deletes a food item.

### Order Management
*   `GET /api/orders`: Retrieves a list of all orders.
*   `POST /api/orders`: Creates a new order.

### Billing
*   `GET /api/bills`: Retrieves a list of all bills.
*   `POST /api/bills`: Creates a new bill.

### Discounts
*   `GET /api/discounts`: Retrieves a list of all available discounts.
*   `POST /api/discounts`: Creates a new discount.

### User Management
*   `GET /api/users`: Retrieves a list of all users (for admins).
*   `POST /api/users`: Creates a new user.

### Ingredient Management
*   `GET /api/ingredients`: Retrieves a list of all ingredients.
*   `POST /api/ingredients`: Adds a new ingredient.

### Restaurant Information
*   `GET /api/restaurant-info`: Retrieves the restaurant's information.
*   `POST /api/restaurant-info`: Updates the restaurant's information.

### Search
*   `GET /api/search/food`: Performs a semantic search for food items based on a query.

### Statistics
*   `GET /api/statistics/summary`: Retrieves a summary of restaurant statistics.

### Chatbot
*   `POST /api/chatbot/chat`: Sends a message to the AI chatbot and gets a response.

### Real-time Chat (WebSocket)
*   **Endpoint:** `/ws`
*   **Application Prefix:** `/app`
*   **Topic:** `/topic/public`
*   **Send Message:** Send a message to `/app/chat.sendMessage`.
*   **Subscribe:** Subscribe to `/topic/public` to receive messages.
