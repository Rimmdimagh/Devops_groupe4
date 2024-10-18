FROM openjdk:11-jre-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file into the Docker image
COPY target/kaddem-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8082

# Set environment variables
ENV SPRINGPROFILES=prod

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar", "-Dspring.profiles.active=${SPRINGPROFILES}", "-Dserver.port=8082", "-Dserver.address=0.0.0.0"]
