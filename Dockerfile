FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/*.jar /app/etudiant-service.jar
EXPOSE 8052
ENTRYPOINT ["java",  "-jar", "/app/etudiant-service.jar"]
