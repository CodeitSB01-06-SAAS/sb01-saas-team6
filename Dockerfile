# 1. Build stage
FROM openjdk:17-jdk-slim AS builder
RUN adduser --disabled-password --gecos '' appuser

WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

RUN chmod +x ./gradlew && \
        ./gradlew dependencies

COPY src ./src

RUN ./gradlew build -x test --no-daemon

# 2. Runtime stage
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

# 애플리케이션 포트 선언
EXPOSE 8080

# 런타임 헬스체크
HEALTHCHECK --interval=30s --timeout=5s --retries=3 CMD curl -f http://localhost:8080/actuator/health || exit 1

# 권장: non-root 실행
USER appuser

ENTRYPOINT ["java", "-jar", "app.jar"]