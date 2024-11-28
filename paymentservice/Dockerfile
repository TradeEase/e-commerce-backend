# Dockerfile for Order Service
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/paymentservice-0.0.1-SNAPSHOT.jar paymentservice-0.0.1-SNAPSHOT.jar
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "paymentservice-0.0.1-SNAPSHOT.jar"]
