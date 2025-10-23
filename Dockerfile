# ============================================
# Multi-stage build for optimal image size
# ============================================

# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Log build start
RUN echo "========================================" && \
    echo "DOCKERFILE - Starting build stage" && \
    echo "========================================" && \
    echo "Maven version:" && mvn -version && \
    echo "Java version:" && java -version

# Copy pom.xml first for better caching
COPY pom.xml .
RUN echo "DOCKERFILE - Downloading dependencies..." && \
    mvn dependency:go-offline -B && \
    echo "DOCKERFILE - Dependencies downloaded successfully"

# Copy source code
COPY src ./src
RUN echo "DOCKERFILE - Source code copied" && \
    ls -la

# Build the application
RUN echo "DOCKERFILE - Building application with Maven..." && \
    mvn clean package -DskipTests -B && \
    echo "DOCKERFILE - Build completed successfully" && \
    ls -la target/

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Log runtime stage
RUN echo "========================================" && \
    echo "DOCKERFILE - Starting runtime stage" && \
    echo "========================================" && \
    echo "Java version:" && java -version

# Copy JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Verify JAR was copied
RUN echo "DOCKERFILE - JAR file copied:" && \
    ls -lh app.jar

# Create non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring && \
    chown spring:spring app.jar && \
    echo "DOCKERFILE - Created non-root user 'spring'"

USER spring:spring

# Expose port (Railway will provide via $PORT)
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:${PORT:-8080}/actuator/health || exit 1

# Log startup and run application
ENTRYPOINT echo "========================================" && \
           echo "DOCKERFILE - Starting Spring Boot application" && \
           echo "========================================" && \
           echo "PORT: ${PORT:-8080}" && \
           echo "DATABASE_URL: ${DATABASE_URL:-NOT_SET}" && \
           echo "PGUSER: ${PGUSER:-NOT_SET}" && \
           echo "Memory: $(free -m | awk 'NR==2{printf \"Total: %sMB, Used: %sMB, Free: %sMB\", $2, $3, $4}')" && \
           echo "========================================" && \
           exec java -XX:+UseContainerSupport \
                -XX:MaxRAMPercentage=75.0 \
                -Djava.security.egd=file:/dev/./urandom \
                -jar app.jar
