FROM openjdk:17-alpine
RUN apk add --no-cache curl
EXPOSE 8082
RUN curl -o app.jar http://admin:nexus@192.168.50.4:8081/repository/maven-releases/tn/esprit/kaddem/0.0.1/kaddem-0.0.1.jar
WORKDIR /
ENV SPRINGPROFILES=prod
ENTRYPOINT ["java", "-jar", "app.jar","-Dspring.profiles.active=${SPRINGPROFILES}","-jar", "-Dserver.port=8082", "-Dserver.address=0.0.0.0"]