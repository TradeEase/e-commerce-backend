# Dockerfile for Spring Cloud Gateway
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/admin-service-0.0.1-SNAPSHOT.jar admin-service-0.0.1-SNAPSHOT.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "admin-service-0.0.1-SNAPSHOT.jar"]
