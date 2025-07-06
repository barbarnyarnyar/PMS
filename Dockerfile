# Self-contained Dockerfile - Installs Maven and builds everything

# Stage 1: Build stage with Maven installation
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

# Install Maven manually
RUN apt-get update && \
    apt-get install -y curl && \
    curl -fsSL https://archive.apache.org/dist/maven/maven-3/3.9.5/binaries/apache-maven-3.9.5-bin.tar.gz | tar xzf - -C /opt && \
    ln -s /opt/apache-maven-3.9.5 /opt/maven && \
    rm -rf /var/lib/apt/lists/*

# Set Maven environment variables
ENV MAVEN_HOME=/opt/maven
ENV PATH="$MAVEN_HOME/bin:$PATH"

# Verify Maven installation
RUN mvn --version

# Copy project files
COPY pom.xml .

# Download dependencies (this will be cached)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests -B

# Verify JAR was created
RUN ls -la target/

# Stage 2: Runtime stage
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Install curl for health checks
RUN apt-get update && \
    apt-get install -y curl && \
    rm -rf /var/lib/apt/lists/*

# Copy JAR from builder stage
COPY --from=builder /app/target/pms-1.0.0.jar app.jar

# Verify JAR was copied
RUN ls -la app.jar

# Create non-root user for security
RUN groupadd -r pmsapp && useradd -r -g pmsapp pmsapp
RUN chown pmsapp:pmsapp app.jar
USER pmsapp

# Expose port
EXPOSE 8080

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=docker
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseContainerSupport"

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]