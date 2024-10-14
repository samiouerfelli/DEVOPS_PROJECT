FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/*.jar /app/discovery-server.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar","-Dspring.profiles.default=${SPRING_PROFILE:-prod}", "/app/discovery-server.jar"]