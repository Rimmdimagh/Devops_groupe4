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

     stage('Build Docker Image') {
         steps {
             script {
                 // Construire l'image Docker
                 def image = docker.build("dorrazorgui/alpine:1.0.0")
             }
         }
     }


                stage('Push Docker Image to Dockerhub') {
                    steps {
                        script {
                            withDockerRegistry([credentialsId: 'docker']) {
                                def image = docker.image('dorrazorgui/alpine:1.0.0')
                                image.push()
                            }
                        }
                    }
                }



          stage('Docker Compose') {
                    steps {
                        dir('/vagrant/project') {


                            // Étape 4 : Lancer Docker Compose
                            sh 'docker compose up -d'
                        }
                    }
                }
    }


  post {
        success {
            echo 'Pipeline succeeded. Sending success email...'
            emailext(
                to: 'dorra.zorgui@esprit.tn',
                subject: "Pipeline Jenkins - Success - Build #${BUILD_NUMBER}",
                body: """<p>The build was successful!</p>
                         <p>Check the details here: <a href="${BUILD_URL}">${BUILD_URL}</a></p>""",
                mimeType: 'text/html'
            )
        }
        failure {
            echo 'Pipeline failed. Sending failure email...'
            emailext(
                to: 'dorra.zorgui@esprit.tn',
                subject: "Pipeline Jenkins - Failure - Build #${BUILD_NUMBER}",
                body: """<p>The build failed.</p>
                         <p>Check the details here: <a href="${BUILD_URL}">${BUILD_URL}</a></p>""",
                mimeType: 'text/html'
            )
        }
        always {
            echo 'Pipeline completed.'
        }
    }
}
