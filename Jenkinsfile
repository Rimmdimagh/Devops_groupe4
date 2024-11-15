pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                script {
                    // Checkout the specified branch from Git
                    git branch: 'AzizMkadem-5BI3-G4', url: 'https://github.com/Rimmdimagh/5BI3-G4_Kaddem.git', credentialsId: 'bcbb38e8-c379-4043-8305-9c6684eb9f33'
                }
            }
        }

        stage('List Files') {
            steps {
                script {
                    // Remove target directory (make sure this is the correct path)
                    sh 'rm -rf target'
                }
            }
        }

        stage('Install Dependencies') {
            steps {
                script {
                    // Install project dependencies using Maven
                    sh 'mvn clean install -DskipTests=true'
                }
            }
        }

        stage('Package') {
            steps {
                script {
                    // Run Maven clean and package
                    sh 'mvn clean package'
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    // Run tests using Maven
                    sh 'mvn test'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    // Run SonarQube analysis using Maven, passing the credentials for authentication
                    sh "mvn sonar:sonar -Dsonar.login=admin -Dsonar.password=Admin@dmin123"
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                script {
                    // Deploy artifact to Nexus repository
                    sh "mvn deploy -Dmaven.test.skip=true"
                }
            }
        }

        stage('Remove Old Docker Image') {
            steps {
                script {
                    // Remove the old Docker image (force remove)
                    sh 'docker rmi -f azizmkadem/kaddemback:latest || true'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build a new Docker image
                    sh 'docker build -t azizmkadem/kaddemback .'
                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', usernameVariable: 'docker_username', passwordVariable: 'docker_password')]) {
                    // Login to Docker Hub and push the image
                    sh 'echo "$docker_password" | docker login -u "$docker_username" --password-stdin'
                    sh 'docker push azizmkadem/kaddemback:latest'
                }
            }
        }

        stage('Docker Compose Down') {
            steps {
                // Bring down the Docker Compose setup
                sh 'docker compose down'
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                // Bring up the Docker Compose setup in detached mode
                sh 'docker compose up -d'
            }
        }

        stage('Restart Prometheus & Grafana') {
            steps {
                echo 'Restarting containers:'
                // Restart Prometheus and Grafana containers
                sh 'docker restart prometheus'
                sh 'docker restart grafana'
            }
        }
    }
}
