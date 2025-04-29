# ------------ STAGE 1: Build ------------
    FROM maven:3.9-eclipse-temurin-21-alpine AS builder

    WORKDIR /build
    COPY pom.xml .
    COPY src ./src
    RUN mvn clean install -DskipTests
    
    # ------------ STAGE 2: Run ------------
    FROM eclipse-temurin:21-jdk-alpine
    
    WORKDIR /app
    COPY --from=builder /build/target/appointment-system-0.0.1-SNAPSHOT.jar app.jar
    
    EXPOSE 8080
    ENTRYPOINT ["java", "-jar", "app.jar"]
    