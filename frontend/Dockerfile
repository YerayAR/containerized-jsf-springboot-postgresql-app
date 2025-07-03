# Build stage
FROM maven:3.8-jdk-11 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q dependency:resolve
COPY src ./src
RUN mvn -q package -DskipTests

# Runtime stage
FROM tomcat:9-jdk11
COPY --from=build /app/target/frontend.war /usr/local/tomcat/webapps/app.war
CMD ["catalina.sh", "run"]
