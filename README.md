# Tax Aggregation System

This project provides a clean, modular, and scalable Spring Boot application boilerplate for a Tax Aggregation System.
It follows a layered architecture with clear separation of concerns, making it easy to understand, maintain, and extend.

## Project Structure

The project is organized into a multi-module Gradle build, inspired by the `customer-fulfillment-api` architecture:

-   **`autotax-domain`**: Contains core business logic, entities (e.g., `Tax`), and value objects. It defines the business domain, free from infrastructure concerns.
-   **`autotax-dao`**: Houses the Data Access Objects (DAOs), typically Spring Data JPA repositories (e.g., `TaxRepository`), for interacting with the database.
-   **`autotax-service`**: Defines service interfaces (e.g., `TaxService`) that declare the business operations. This module depends on the `domain` module.
-   **`autotax-service-impl`**: Provides the concrete implementations of the service interfaces defined in the `service` module (e.g., `TaxServiceImpl`). It depends on `service`, `dao`, and `domain` modules.
-   **`autotax-integration`**: Defines interfaces and contracts for interacting with external systems or services (e.g., `ExternalTaxService`). It depends on the `domain` module.
-   **`autotax-integration-impl`**: Contains the concrete implementations for the integration interfaces (e.g., `ExternalTaxServiceImpl`). It depends on `integration` and `domain` modules.
-   **`autotax-report`**: Dedicated to generating various reports (e.g., `TaxReportService`). It depends on `domain` and `service` modules to fetch data.
-   **`autotax-infrastructure`**: A module for broader infrastructure concerns, such as common configurations, security aspects, utility classes, or cross-cutting concerns. It can depend on `domain`, `dao`, `service`, `service-impl`, `integration`, `integration-impl`, and `report` as needed.
-   **`autotax-web`**: Exposes REST APIs using Spring Web. It contains controllers (e.g., `TaxController`) and Data Transfer Objects (DTOs). It primarily interacts with the `service` module (via its interface) and relies on Spring to inject the `service-impl` bean. It may also interact with `integration` and `report` modules.
-   **`autotax-test-starter`**: Aggregates common testing dependencies and configurations, ensuring a consistent testing setup across all modules.

## Technologies Used

-   **Java 17**
-   **Spring Boot 3.2.0**
-   **Gradle** (build tool)
-   **Lombok** (to reduce boilerplate code)
-   **Spring Data JPA** (for database interaction)
-   **H2 Database** (in-memory for development and testing)
-   **Spring Web** (for REST APIs)
-   **SpringDoc OpenAPI** (for API documentation)

## Getting Started

### Prerequisites

-   Java Development Kit (JDK) 17 or later
-   Gradle (optional, as `./gradlew` wrapper is included)

### Build the Project

**IMPORTANT: Ensure your `JAVA_HOME` environment variable is set to a JDK 17 (or newer) installation.**

1.  Navigate to the root directory of the project (`autotax`):
    ```bash
    cd C:\jcodes\projects\autotax
    ```
2.  Initialize the Gradle wrapper (if not already present, this will create `gradlew` and `gradlew.bat` files):
    ```bash
    gradle wrapper
    ```
    (On Windows, you might just need `gradle wrapper` if `gradle` is in your PATH, or ensure you have Gradle installed globally.)
3.  Build the project:
    ```bash
    .\gradlew.bat clean build
    ```

### Run the Application

After building, you can run the application from the `autotax-web` module:

```bash
.\gradlew.bat :autotax-web:bootRun
```

The application will start on `http://localhost:8080`.

### API Documentation

Once the application is running, you can access the OpenAPI (Swagger UI) documentation at:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### H2 Console

For inspecting the in-memory H2 database, you can access the H2 console at:

[http://localhost:8080/h2-console](http://localhost:8080/h2-console)

Use the following credentials:
-   **JDBC URL:** `jdbc:h2:mem:taxdb`
-   **User Name:** `sa`
-   **Password:** (leave blank)

## Example API Endpoints

Here are some example API endpoints provided by the boilerplate:

-   **GET /api/taxes**: Get all taxes.
-   **GET /api/taxes/{id}**: Get a tax by ID.
-   **GET /api/taxes/taxpayer/{taxpayerId}**: Get taxes by taxpayer ID.
-   **POST /api/taxes**: Create a new tax (requires `TaxDto` in request body).
-   **PUT /api/taxes/{id}**: Update an existing tax (requires `TaxDto` in request body).
-   **DELETE /api/taxes/{id}**: Delete a tax by ID.

## Extending the System

To extend this system, follow the modular structure:

1.  **Domain:** Define new entities, value objects, or interfaces.
2.  **DAO:** Create new repositories for data access.
3.  **Service:** Define new service interfaces for business operations.
4.  **Service-Impl:** Provide concrete implementations for the new service interfaces.
5.  **Integration:** Define interfaces for external system interactions.
6.  **Integration-Impl:** Provide concrete implementations for external system integrations.
7.  **Report:** Implement logic for generating various reports.
8.  **Infrastructure:** Add common configurations, security, or utility classes.
9.  **Web:** Add new controllers or DTOs to expose new functionalities via REST APIs.

Remember to update the `build.gradle` files in the respective modules if you add new dependencies.
