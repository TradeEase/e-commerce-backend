# Dockerfile for Spring Cloud Gateway
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/spring-cloud-service-0.0.1-SNAPSHOT.jar spring-cloud-service-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "spring-cloud-service-0.0.1-SNAPSHOT.jar"]