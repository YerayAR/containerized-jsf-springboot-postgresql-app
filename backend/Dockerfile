# Build stage
FROM maven:3.8-jdk-11 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q dependency:resolve
COPY src ./src
RUN mvn -q package -DskipTests

# Runtime stage
FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /app/target/backend-1.0.0.jar backend.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/backend.jar"]
