# FROM : Définir depuis quelle base votre image va être créée
FROM openjdk:17-jdk-alpine


# Installer curl
RUN apk --no-cache add curl

# ENV : Définir des variables d’environnement
ENV APP_NAME="Kaddem" \
    APP_VERSION="1.0.0"



# WORKDIR : Définir le dossier de travail pour toutes les autres commandes
WORKDIR /app



# COPY : Copier le fichier JAR généré par ton projet dans le conteneur
COPY target/kaddem-0.0.1-SNAPSHOT.jar /app/kaddem-app.jar

# RUN : Lancer une commande lors de la construction de l'image
RUN echo "Building Docker image for the Kaddem project"

# EXPOSE : Exposer le port 8089 pour permettre l'accès à l'application Spring Boot
EXPOSE 8089

# VOLUMES : Créer un point de montage pour persister les données
VOLUME ["/app/data"]

# ENTRYPOINT : Définir une commande qui sera exécutée par défaut
ENTRYPOINT ["java", "-jar", "/app/kaddem-app.jar"]


