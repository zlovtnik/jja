FROM --platform=linux/arm64 amazoncorretto:21-alpine as builder
WORKDIR /app

# Install necessary build tools
RUN apk add --no-cache curl maven

# Copy source code and pom.xml
COPY pom.xml .
COPY src src

# Build the application with Spring Boot Maven plugin
RUN mvn clean package spring-boot:repackage -DskipTests

FROM --platform=linux/arm64 amazoncorretto:21-alpine
WORKDIR /app

# Install curl for healthcheck
RUN apk add --no-cache curl

# Copy the built jar from builder
COPY --from=builder /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application with specific JVM options for ARM and Spring Boot
ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+UseStringDeduplication -XX:+UseContainerSupport -XX:MaxRAMPercentage=75"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 