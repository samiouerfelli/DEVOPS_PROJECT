FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/*.jar /app/universite-service.jar
EXPOSE 8055
ENTRYPOINT ["java", "-Dspring.profiles.default=prod", "-jar", "/app/universite-service.jar"]