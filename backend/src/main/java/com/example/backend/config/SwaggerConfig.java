package com.example.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger/OpenAPI 3 Configuration
 * 
 * This configuration provides comprehensive API documentation for the 
 * Containerized JSF Spring Boot PostgreSQL Application with Hibernate.
 * 
 * Access URLs:
 * - Swagger UI: http://localhost:8080/swagger-ui.html
 * - OpenAPI JSON: http://localhost:8080/v3/api-docs
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Product Catalog API")
.description("Containerized JSF Spring Boot PostgreSQL Application. " +
                    "This is a comprehensive REST API for managing products in a catalog system. " +
                    "The application uses Spring Boot 2.6.0 as the main framework, " +
                    "Hibernate (native) as the persistence layer (migrated from generic JPA), " +
                    "PostgreSQL as the database, Spring Security with JWT authentication, " +
                    "JSF as the frontend framework, and Docker for containerization. " +
                    "Features include: Full CRUD operations for products, Advanced filtering and pagination, " +
                    "JWT-based authentication, Hibernate native optimizations, Soft delete functionality, " +
                    "Input validation, and RESTful design principles. " +
                    "Most endpoints require JWT authentication. Use the /api/auth/login endpoint " +
                    "to obtain a token, then include it in the Authorization header as Bearer <token>.")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Development Team")
                    .email("dev@example.com")
                    .url("https://example.com"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")))
            .servers(List.of(
                new Server()
                    .url("http://localhost:8080")
                    .description("Local Development Server"),
                new Server()
                    .url("https://api.example.com")
                    .description("Production Server")))
            .components(new Components()
                .addSecuritySchemes("JWT", new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .description("JWT token for API authentication")))
            .addSecurityItem(new SecurityRequirement().addList("JWT"));
    }
}
