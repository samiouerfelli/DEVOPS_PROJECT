FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/*.jar /app/foyer-service.jar
EXPOSE 8053
ENTRYPOINT ["java", "-jar", "/app/foyer-service.jar"]
