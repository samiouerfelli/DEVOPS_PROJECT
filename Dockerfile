# Use Maven with OpenJDK 17 for the build stage
FROM maven:3.8.4-openjdk-17 as builder
WORKDIR /app
COPY . .
# Run Maven build with JDK 17
RUN mvn clean package -DskipTests

# Use JDK 17 for the runtime environment
FROM openjdk:17-jdk-slim
COPY --from=builder /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
