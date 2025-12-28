# =============================================================================
# 5TPROMART - Multi-stage Dockerfile
# =============================================================================
# Stage 1: Build with Maven
# Stage 2: Run with minimal JRE
# =============================================================================

# ---------------------------------------------------------------------------
# Stage 1: Build
# ---------------------------------------------------------------------------
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copy Maven wrapper and pom.xml first (for layer caching)
COPY mvnw ./
COPY .mvn .mvn
COPY pom.xml ./

# Make mvnw executable
RUN chmod +x mvnw

# Download dependencies (cached layer)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application (skip tests for faster builds)
RUN ./mvnw package -DskipTests -B

# ---------------------------------------------------------------------------
# Stage 2: Run
# ---------------------------------------------------------------------------
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Health check endpoint
HEALTHCHECK --interval=30s --timeout=10s --retries=3 --start-period=30s \
    CMD wget -q --spider http://localhost:8080/actuator/health || exit 1

# Expose application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
