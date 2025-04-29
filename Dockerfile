# ------------ STAGE 1: Build the JAR ------------
    FROM maven:3.9.3-eclipse-temurin-21 AS builder

    WORKDIR /build
    COPY pom.xml .
    COPY src ./src
    RUN mvn clean install -DskipTests
    
    # ------------ STAGE 2: Run the JAR ------------
    FROM eclipse-temurin:21-jdk
    
    WORKDIR /app
    COPY --from=builder /build/target/appointment-system-0.0.1-SNAPSHOT.jar app.jar
    
    EXPOSE 8080
    ENTRYPOINT ["java", "-jar", "app.jar"]
    