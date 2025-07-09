
FROM openjdk:17-jdk-slim

# --- 런타임 필수 패키지 설치 --------------------------
RUN apt-get update && \
    apt-get install -y --no-install-recommends curl && \
    rm -rf /var/lib/apt/lists/*
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