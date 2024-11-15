pipeline {
    agent any



    stages {
        stage('Checkout') {
            steps {
                script {
                    // Checkout the specified branch from Git
                    git branch: 'AzizMkadem-5BI3-G4', url: 'https://github.com/Rimmdimagh/5BI3-G4_Kaddem.git',credentialsId: 'bcbb38e8-c379-4043-8305-9c6684eb9f33'
                }
            }
        }

        stage('List Files') {
    steps {
        script {
            sh 'rm -rf /target'
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
        stage('package') {
            steps {
                script {
                    // Install project dependencies using Maven
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
                 //   withCredentials([usernamePassword(credentialsId: 'sonar-credentials', usernameVariable: 'SONAR_USER', passwordVariable: 'SONAR_PASSWORD')]) {
                        sh "mvn sonar:sonar -Dsonar.login=admin -Dsonar.password=Admin@dmin123"
                    }
                }
            }

        stage('Deploy to Nexus') {
            steps {
              // withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASSWORD')]) {
                    sh "mvn deploy -Dmaven.test.skip=true "  //-Dusername=$NEXUS_USER -Dpassword=$NEXUS_PASSWORD
                }
            }

         stage('Remove Old Docker Image') {
            steps {
                script {
                    sh 'docker rmi -f azizmkadem/kaddemback:latest || true' // -f forces removal, and || true ignores errors
                }
            }
        }
//
        stage('Build Docker Image') {
            steps {
                script {

                    sh 'docker build -t azizmkadem/kaddemback .'

                }
            }
        }

        stage('push docker image to docker hub') {
	    steps {
		withcredentials([usernamepassword(credentialsid: 'docker-hub-credentials', usernamevariable: 'docker_username', passwordvariable: 'docker_password')]) {
		    sh 'echo "$docker_password" | docker login -u "$docker_username" --password-stdin'
		    sh 'docker push azizmkadem/kaddemback:latest'
		}

	}

        stage('Docker Compose Down') {
            steps {

                    sh 'docker compose down'

            }
        }

        stage('Deploy with Docker Compose') {
            steps {

                    sh 'docker compose up -d'
                }

        }
        stage('restarting prometheus & grafana') {
                                        steps {
                                            echo 'Containers restarted :'
                                            sh 'docker restart prometheus '
                                            sh 'docker restart grafana '
                                        }
                                    }
            }


    }
}
