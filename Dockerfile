# -------- Stage 1: Build --------
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copy Maven files first (better caching)
COPY pom.xml .
COPY src ./src

# Build the application
RUN ./mvnw -q clean package -DskipTests || mvn -q clean package -DskipTests

# -------- Stage 2: Run --------
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
