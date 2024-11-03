# Start with OpenJDK 17 as the base image
FROM openjdk:17-jdk

# Create a non-root user
RUN adduser --system --no-create-home appuser

# Switch to the new user
USER appuser

# Copy the JAR file to the image
COPY target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]
