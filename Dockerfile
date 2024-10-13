FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/*.jar /app/bloc-service.jar
EXPOSE 8050
ENTRYPOINT ["java", "-Dspring.profiles.default=prod", "-jar", "/app/bloc-service.jar"]
