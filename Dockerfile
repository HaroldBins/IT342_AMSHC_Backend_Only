# Use OpenJDK 21 as base image
FROM eclipse-temurin:21-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project files
COPY pom.xml /app/
COPY src /app/src/

# Install Maven and build the project
RUN apt-get update && apt-get install -y maven && mvn clean install

# Run the Spring Boot application
CMD ["java", "-jar", "target/appointment-system-0.0.1-SNAPSHOT.jar"]

