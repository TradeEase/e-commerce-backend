# Dockerfile for Order Service
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/order-service-0.0.1-SNAPSHOT.jar order-service-0.0.1-SNAPSHOT.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "order-service-0.0.1-SNAPSHOT.jar"]
