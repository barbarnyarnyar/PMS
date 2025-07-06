# Multi-stage build - Maven + Java build inside Docker
FROM maven:3.9.5-openjdk-17-slim AS builder

WORKDIR /app

# Copy Maven files first (for better caching)
COPY pom.xml .

# Download dependencies (this layer will be cached)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests -B

# Runtime stage
FROM openjdk:17-jre-slim

WORKDIR /app

# Copy JAR from builder stage
COPY --from=builder /app/target/pms-1.0.0.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]