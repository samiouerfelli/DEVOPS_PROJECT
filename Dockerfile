FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/*.jar /app/etudiant-service.jar
EXPOSE 8052
ENTRYPOINT ["java", "-Dspring.profiles.default=prod", "-jar", "/app/etudiant-service.jar"]