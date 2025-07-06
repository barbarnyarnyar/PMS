# Multi-stage Docker build for Tolimoli Hotel PMS
# Stage 1: Build the application
# Alternative options if maven:3.9.5-openjdk-17-slim is slow:
# FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder
# FROM maven:3.9.5-openjdk-17 AS builder
FROM maven:3.9.5-openjdk-17-slim AS builder

# Set working directory
WORKDIR /app

# Copy Maven files for dependency resolution
COPY pom.xml .
COPY .mvn/ .mvn/
COPY mvnw .

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests -B

# Stage 2: Create the runtime image
FROM openjdk:17-jre-slim

# Install additional packages for production
RUN apt-get update && apt-get install -y \
    curl \
    wget \
    dumb-init \
    && rm -rf /var/lib/apt/lists/* \
    && apt-get clean

# Create application user (security best practice)
RUN groupadd -r pmsapp && useradd -r -g pmsapp pmsapp

# Set working directory
WORKDIR /app

# Create necessary directories
RUN mkdir -p /app/logs /app/temp /app/uploads \
    && chown -R pmsapp:pmsapp /app

# Copy the JAR file from builder stage
COPY --from=builder /app/target/pms-*.jar app.jar

# Copy additional configuration files if needed
COPY --from=builder /app/src/main/resources/application*.yml ./config/

# Change ownership of application files
RUN chown -R pmsapp:pmsapp /app

# Switch to non-root user
USER pmsapp

# Set JVM options for containerized environment
ENV JAVA_OPTS="-XX:+UseContainerSupport \
               -XX:MaxRAMPercentage=75.0 \
               -XX:+UseG1GC \
               -XX:+UseStringDeduplication \
               -XX:+PrintGCDetails \
               -XX:+PrintGCTimeStamps \
               -Djava.security.egd=file:/dev/./urandom \
               -Dspring.profiles.active=docker"

# Set application-specific environment variables
ENV SERVER_PORT=8080
ENV LOG_LEVEL=INFO
ENV TZ=UTC

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:${SERVER_PORT}/actuator/health || exit 1

# Expose the application port
EXPOSE ${SERVER_PORT}

# Use dumb-init to handle signals properly
ENTRYPOINT ["dumb-init", "--"]

# Start the application
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

# Metadata
LABEL maintainer="Tolimoli PMS Team <dev@tolimoli.com>" \
      version="1.0.0" \
      description="Tolimoli Hotel Property Management System" \
      org.opencontainers.image.title="Tolimoli PMS" \
      org.opencontainers.image.description="All-in-one Hotel Management System" \
      org.opencontainers.image.version="1.0.0" \
      org.opencontainers.image.vendor="Tolimoli" \
      org.opencontainers.image.source="https://github.com/tolimoli/pms"