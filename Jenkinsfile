pipeline {
    agent any

    stages {
        stage('Checkout Git') {
            steps {
                git url: 'https://github.com/Rimmdimagh/5BI3-G4_Kaddem.git', branch: 'DorraZorguiKaddem'
            }
        }

        stage('Check and Start MySQL') {
            steps {
                script {
                    // Vérifie si MySQL est actif, sinon le démarre
                    sh 'systemctl is-active --quiet mysql || systemctl start mysql'
                }
            }
      }

        stage('docker compose') {
            steps {
                script {
                          // Assurez-vous que le fichier docker-compose.yml existe dans le repo
                          sh 'docker compose down'
                          sh 'docker compose up -d'
                      }
                }
              }
        stage('Start Prometheus') {
                    steps {
                        script {
                            // Lancer Prometheus via Docker
                            sh """
                                docker run -d \
                                --name prometheus \
                                -p 9090:9090 \
                                -v \$(pwd)/${PROMETHEUS_CONFIG}:/etc/prometheus/prometheus.yml \
                                prom/prometheus
                            """
                        }
                    }
                }

        stage('Start Grafana') {
                    steps {
                        script {
                            // Lancer Grafana via Docker
                            sh """
                                docker run -d \
                                --name grafana \
                                -p 3000:3000 \
                                grafana/grafana
                            """
                        }
                    }
                }

        // Ajoute d'autres étapes comme le build, les tests, etc. ici

        stage('Maven Build') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Tests - JUnit/Mockito') {
            steps {
                sh 'mvn test'
            }
        }




    }
post {
    success {
        echo 'La pipeline s\'est terminée avec succès. Aucune action requise.'
    }
    failure {
        echo 'La pipeline a échoué. Veuillez vérifier les logs de Jenkins pour plus de détails.'
    }
}

}