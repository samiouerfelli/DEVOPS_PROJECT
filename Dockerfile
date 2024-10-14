FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/*.jar /app/chambre-service.jar
EXPOSE 8051
ENTRYPOINT ["java",  "-jar", "/app/chambre-service.jar"]
