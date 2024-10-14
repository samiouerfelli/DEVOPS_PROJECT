FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/*.jar /app/gateway-service.jar
EXPOSE 9999
ENTRYPOINT ["java", "-Dspring.profiles.default=prod", "-jar", "/app/gateway-service.jar"]