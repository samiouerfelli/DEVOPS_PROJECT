# Start with a base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the target directory to the container
COPY target/your-app-name-version.jar /app/app.jar

# Expose the port the application will run on
EXPOSE 8080

# Command to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
