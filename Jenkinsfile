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
 stage('SonarQube Analysis') {
            steps {
                        sh 'mvn sonar:sonar -Dsonar.host.url=http://192.168.50.4:9100 -Dsonar.login=admin -Dsonar.password=Dorrazorgui2025@1'
                    }
                }




        stage('Deploy to Nexus') {
                    steps {
                                echo 'Deploying to Nexus Repository'
                                // Remplacez <nexus-ip> par l'adresse IP réelle de votre serveur Nexus
                                sh 'mvn clean deploy -DskipTests'
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

    post {
      success {
        echo 'La pipeline s\'est terminée avec succès. Aucune action requise.'
      }
      failure {
        echo 'La pipeline a échoué. Veuillez vérifier les logs de Jenkins pour plus de détails.'
      }
    }

}