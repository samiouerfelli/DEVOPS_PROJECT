FROM openjdk:17-jdk

RUN adduser --system --group appuser
USER appuser

COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
