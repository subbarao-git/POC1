pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "subbu2712/my-java-app"
    }

    stages {

        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn clean test'
            }
        }

        stage('SonarQube Analysis') {
            environment {
                SONAR_TOKEN = credentials('sonar-token')
            }
            steps {
                sh """
                mvn sonar:sonar \
                -Dsonar.projectKey=my-java-app \
                -Dsonar.host.url=http://sonarqube:9000 \
                -Dsonar.login=$SONAR_TOKEN
                """
            }
        }

        stage('Package') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t $DOCKER_IMAGE .'
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(
                  credentialsId: 'dockerhub-creds',
                  usernameVariable: 'DOCKER_USER',
                  passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh """
                    docker login -u $DOCKER_USER -p $DOCKER_PASS
                    docker push $DOCKER_IMAGE
                    """
                }
            }
        }
    }

    post {
        success {
            echo "✅ Build & Docker Push Successful!"
        }
        failure {
            echo "❌ Pipeline Failed!"
        }
    }
}
