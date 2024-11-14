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
                    // Check if MySQL is active, and start it if not
                    sh 'systemctl is-active --quiet mysql || systemctl start mysql'
                }
            }
        }

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

        stage('SonarQube Analysis') {
            steps {
                script {
                    // Correct SonarQube URL and ensure proper configuration
                    sh 'mvn sonar:sonar -Dsonar.host.url=http://192.168.50.4:9100 -Dsonar.login=admin -Dsonar.password=Dorrazorgui2025@1'
                }
            }
        }

        stage('Docker Compose') {
            steps {
                script {
                    // Ensure docker-compose commands are executed
                    sh '''
                        docker compose down || echo "Docker Compose already down."
                        docker compose up -d
                    '''
                }
            }
        }
    }

    post {
        success {
            echo 'The pipeline completed successfully. No action required.'
        }
        failure {
            echo 'The pipeline failed. Please check the Jenkins logs for more details.'
        }
    }
}
