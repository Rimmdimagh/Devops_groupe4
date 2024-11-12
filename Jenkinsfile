pipeline {
    agent any

    stages {
        stage('Checkout Git') {
            steps {
                git url: 'https://github.com/Rimmdimagh/5BI3-G4_Kaddem.git', branch: 'DorraZorgui'
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

        stage('dockkkkkkk') {
            steps {
                script {
                          // Assurez-vous que le fichier docker-compose.yml existe dans le repo
                          sh 'docker-compose -f docker-compose.yml up -d'
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