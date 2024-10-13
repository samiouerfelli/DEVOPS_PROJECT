FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/*.jar /app/chambre-service.jar
EXPOSE 8050
ENTRYPOINT ["java", "-Dspring.profiles.default=prod", "-jar", "/app/chambre-service.jar"]