# Use official OpenJDK as base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy build jar file
COPY build/libs/*.jar app.jar

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]