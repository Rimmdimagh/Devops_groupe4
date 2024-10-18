# Use a base image
FROM openjdk:11-jre-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the target directory
COPY target/kaddem-0.0.1-SNAPSHOT.jar app.jar

# Expose the port
EXPOSE 8082

# Command to run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar", "-Dspring.profiles.active=prod", "-Dserver.port=8082", "-Dserver.address=0.0.0.0"]
