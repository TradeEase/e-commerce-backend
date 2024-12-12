# Dockerfile for Order Service
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/product-service-0.0.1-SNAPSHOT.jar product-service-0.0.1-SNAPSHOT.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "product-service-0.0.1-SNAPSHOT.jar"]