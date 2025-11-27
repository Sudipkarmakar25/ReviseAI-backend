# 1. Base image with JDK 17
FROM eclipse-temurin:17-jdk-alpine

# 2. Set working directory
WORKDIR /app

# 3. Copy the built JAR file into the container
COPY target/*.jar app.jar

# 4. Expose port (default for Spring Boot)
EXPOSE 8080

# 5. Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
