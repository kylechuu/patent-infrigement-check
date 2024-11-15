# Step 1: Build the application using Maven inside Docker
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app
# Copy the entire project into the Docker container
COPY . .

# Build the Spring Boot application
RUN ./mvnw clean package -DskipTests

# Step 2: Create a lightweight image to run the built JAR
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy only the JAR file from the previous build stage
COPY --from=builder /app/target/take-home-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
