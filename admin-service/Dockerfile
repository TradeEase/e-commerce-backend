# Dockerfile for Spring Cloud Gateway
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/admin-service-0.0.1-SNAPSHOT.jar admin-service-0.0.1-SNAPSHOT.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "admin-service-0.0.1-SNAPSHOT.jar"]
