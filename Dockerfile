FROM openjdk:17.0.1-slim

WORKDIR /app

ARG JAR_FILE

COPY ${JAR_FILE} api.jar

EXPOSE 8080

CMD ["java", "-jar", "-Dspring.profiles.active=development", "api.jar"]