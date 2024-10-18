pipeline {
    agent any

    stages {
        stage('Checkout Git') {
            steps {
                git url: 'https://github.com/Rimmdimagh/5BI3-G4_Kaddem.git', branch: 'RymMdimagh-5BI3-G4'
            }
        }
        stage('Check Curl Installation') {
            steps {
                script {
                    // Vérifie si curl est disponible
                    sh 'which curl || echo "curl is not installed"'
                    sh 'curl --version || echo "Unable to get curl version"'
                }
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

        // Ajoute d'autres étapes comme le build, les tests, etc. ici

        stage('Maven Build') {
            steps {
                sh 'mvn clean install'
            }
        }
 stage('Build package') {
            steps {
                sh 'mvn package'
            }
        }

        stage('Tests - JUnit/Mockito') {
            steps {
                sh 'mvn test'
            }
        }
      stage('SonarQube Analysis') {
            steps {
                sh 'mvn sonar:sonar -Dsonar.host.url=http://192.168.100.19:9000 -Dsonar.login=admin -Dsonar.password=Theacefamily12@'
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
