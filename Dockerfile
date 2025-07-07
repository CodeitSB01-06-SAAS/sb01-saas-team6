# 1. Build stage
FROM openjdk:17-jdk-slim AS builder

WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

RUN chmod +x ./gradlew
RUN ./gradlew dependencies

COPY src ./src

RUN ./gradlew build -x test

# 2. Runtime stage
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]