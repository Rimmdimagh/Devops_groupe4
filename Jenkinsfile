pipeline {
    agent any

    stages {
        // Stage 1: Checkout the code from the Git repository
        stage('Checkout Git') {
            steps {
                git url: 'https://github.com/Rimmdimagh/5BI3-G4_Kaddem.git', branch: 'RymMdimagh-5BI3-G4'
            }
        }

        // Stage 2: Check if curl is installed
        stage('Check Curl Installation') {
            steps {
                script {
                    // Vérifie si curl est disponible
                    sh 'which curl || echo "curl is not installed"'
                    sh 'curl --version || echo "Unable to get curl version"'
                }
            }
        }

        // Stage 3: Check MySQL status and start if necessary
        stage('Check and Start MySQL') {
            steps {
                script {
                    // Vérifie si MySQL est actif, sinon le démarre
                    sh 'systemctl is-active --quiet mysql || systemctl start mysql'
                }
            }
        }

        // Stage 4: Build the Maven project
        stage('Maven Build') {
            steps {
                sh 'mvn clean install -DskipTests '
            }
        }



        // Stage 6: Run JUnit/Mockito tests
        stage('Tests - JUnit/Mockito') {
            steps {
                sh 'mvn test'
            }
        }

        // Stage 7: Generate JaCoCo code coverage report
        stage('Generate JaCoCo Report') {
            steps {
                echo 'Generating JaCoCo Report'
                sh 'mvn jacoco:report'
            }
        }

          stage('JaCoCo Coverage Report') {
            steps {
                echo 'Publishing JaCoCo Coverage Report'
                step([$class: 'JacocoPublisher',
                      execPattern: '**/target/jacoco.exec',
                      classPattern: '**/classes',
                      sourcePattern: '**/src',
                      exclusionPattern: '/target/**/,**/*Test,**/*_javassist/**'
                ])
            }
        }

        // Stage 9: SonarQube analysis
        stage('SonarQube Analysis') {
            steps {
                sh 'mvn sonar:sonar -Dsonar.host.url=http://192.168.100.19:9100 -Dsonar.login=admin -Dsonar.password=Theacefamily12@'
            }
        }

        // Stage 10: Deploy the artifact to Nexus repository
        stage('Deploy to Nexus') {
            steps {
                echo 'Deploying to Nexus Repository'
                // Remplacez <nexus-ip> par l'adresse IP réelle de votre serveur Nexus
                sh 'mvn clean deploy -DskipTests'
            }
        }

      stage('Build Docker Image') {
            steps {
                script {
                    // Construire l'image Docker à partir du Dockerfile
                    docker.build("rimmdimagh/kaddem:1.0.0")
                }
            }
        }

        // Stage 12: Push Docker image to DockerHub
        stage('Push Docker Image to Dockerhub') {
            steps {
                script {
                    // Pousser l'image Docker vers Dockerhub
                    withDockerRegistry([credentialsId: 'dockerhub-credentials']) {
                        docker.image('rimmdimagh/kaddem:1.0.0').push()
                    }
                }
            }
        }

        // Stage 13: Deploy with Docker Compose

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
            echo 'Pipeline succeeded. Sending success email...'
            emailext(
                to: 'rim.mdimagh@esprit.tn',
                subject: "Pipeline Jenkins - Success - Build #${BUILD_NUMBER}",
                body: """<p>The build was successful!</p>
                         <p>Check the details here: <a href="${BUILD_URL}">${BUILD_URL}</a></p>""",
                mimeType: 'text/html'
            )
        }
        failure {
            echo 'Pipeline failed. Sending failure email...'
            emailext(
                to: 'rim.mdimagh@esprit.tn',
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
