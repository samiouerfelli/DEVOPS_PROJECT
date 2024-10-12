FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/*.jar /app/config-server.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "/app/config-server.jar"]
