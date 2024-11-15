# FROM : Définir depuis quelle base votre image va être créée
FROM openjdk:17-jdk-alpine
# EXPOSE : Exposer le port 8089 pour permettre l'accès à l'application Spring Boot
EXPOSE 8089
# COPY : Copier le fichier JAR généré par ton projet dans le conteneur
ADD target/kaddem-0.0.1.jar /app/kaddem-app.jar
# ENTRYPOINT : Définir une commande qui sera exécutée par défaut
ENTRYPOINT ["java", "-jar", "/app/kaddem-app.jar"]