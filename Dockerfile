FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/*.jar /app/reservation-service.jar
EXPOSE 8054
ENTRYPOINT ["java", "-Dspring.profiles.default=prod", "-jar", "/app/reservation-service.jar"]