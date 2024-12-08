# Dockerfile for Order Service
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/TaskUserService-0.0.1-SNAPSHOT.jar TaskUserService-0.0.1-SNAPSHOT.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "TaskUserService-0.0.1-SNAPSHOT.jar"]