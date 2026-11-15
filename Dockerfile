# Use Java 27 runtime
FROM eclipse-temurin:27-jre

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged Spring Boot application
COPY target/ledgerly-0.0.1-SNAPSHOT.jar app.jar

# Document the port used by the application
EXPOSE 8080

# Start the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]