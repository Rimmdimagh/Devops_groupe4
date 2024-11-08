FROM openjdk:17
WORKDIR /app
EXPOSE 8085
ADD target/*.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]