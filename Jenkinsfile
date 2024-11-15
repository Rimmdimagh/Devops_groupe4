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
                    // Check if MySQL is active, if not, start it
                    sh 'systemctl is-active --quiet mysql || systemctl start mysql'
                }
            }
        }

        stage('Maven Build') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Run Tests - JUnit/Mockito') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Generate JaCoCo Report') {
            steps {
                echo 'Generating JaCoCo Report'
                sh 'mvn jacoco:report'
            }
        }

        stage('Publish JaCoCo Coverage Report') {
            steps {
                echo 'Publishing JaCoCo Coverage Report'
                step([
                    $class: 'JacocoPublisher',
                    execPattern: '**/target/jacoco.exec',
                    classPattern: '**/classes',
                    sourcePattern: '**/src',
                    exclusionPattern: '/target/**/,**/*Test,**/*_javassist/**'
                ])
            }
        }

        stage('SonarQube Analysis') {
            steps {
                sh 'mvn sonar:sonar -Dsonar.host.url=http://192.168.50.4:9100 -Dsonar.login=admin -Dsonar.password=Dorrazorgui2025@1'
            }
        }

        stage('Deploy to Nexus') {
            steps {
                echo 'Deploying to Nexus Repository'
                // Replace <nexus-ip> with the actual IP of your Nexus server
                sh 'mvn clean deploy -DskipTests'
            }
        }

          stage('Docker Compose') {
                    steps {
                        dir('/vagrant/kaddem') {
                            // Étape 1 : Vérifier les ports occupés
                            sh 'sudo lsof -i -P -n | grep 8100 || true'

                            // Étape 2 : Libérer le port 8100
                            sh 'sudo fuser -k 8100/tcp || true'

                            // Étape 3 : Nettoyer Docker
                            sh 'docker compose down || true'
                            sh 'docker network prune -f || true'

                            // Étape 4 : Lancer Docker Compose
                            sh 'docker compose up -d'
                        }
                    }
                }
    }

    post {
        success {
            echo 'The pipeline completed successfully. No further action is required.'
        }
        failure {
            echo 'The pipeline failed. Please check Jenkins logs for more details.'
        }
    }
}
