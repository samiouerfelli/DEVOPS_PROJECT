FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/*.jar /app/reservation-service.jar
EXPOSE 8054
ENTRYPOINT ["java",  "-jar", "/app/reservation-service.jar"]
