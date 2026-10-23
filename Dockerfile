# -----------------------------------------------------------
# Stage 1: Build the application with Maven
# -----------------------------------------------------------
# Uses an official Maven image that includes JDK 17.
# The build happens inside the container so you don't need
# Java or Maven installed on your host machine.
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copy only the POM first so Maven can cache dependency downloads.
# If pom.xml hasn't changed, Docker reuses this layer on rebuild.
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Now copy source code and build the fat JAR (skipping tests
# to speed up the Docker build — tests should run in CI).
COPY src ./src
RUN mvn clean package -DskipTests -B

# -----------------------------------------------------------
# Stage 2: Run with a slim JRE-only image
# -----------------------------------------------------------
# eclipse-temurin:17-jre-alpine is ~80 MB smaller than the
# full JDK image, reducing container startup time and attack surface.
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the application.
# -Djava.security.egd speeds up SecureRandom on Linux containers.
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
