# Utiliser une image OpenJDK légère basée sur Alpine
FROM openjdk:17-jdk-slim

# Variables d'environnement
ENV APP_NAME="Kaddem" \
    APP_VERSION="1.0.0"

# Créer le répertoire de travail dans le conteneur
WORKDIR /app

# Copier le fichier JAR généré dans le conteneur
COPY target/kaddem-0.0.1-SNAPSHOT.jar /app/kaddem-app.jar

# Exposer le port 8089 pour permettre l'accès à l'application Spring Boot
EXPOSE 8089

# Lancer l'application avec la commande java
ENTRYPOINT ["java", "-jar", "/app/kaddem-app.jar"]
