# Dockerfile for Order Service
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/user-service-0.0.1-SNAPSHOT.jar user-service-0.0.1-SNAPSHOT.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "user-service-0.0.1-SNAPSHOT.jar"]
