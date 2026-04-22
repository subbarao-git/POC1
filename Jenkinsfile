pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "subbu2712/my-java-app1"
        SONAR_TOKEN = "SonarServer"
        EC2_HOST   = "ec2-35-154-196-181.ap-south-1.compute.amazonaws.com"
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
            steps {
                withSonarQubeEnv("${SONAR_TOKEN}") {
                sh """
                mvn sonar:sonar \
                -Dsonar.projectKey=my-java-app
                """
                }
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
        
        stage('Deploy to EC2') {
            steps {
                sshagent(credentials: ['ec2-ssh-key']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no ubuntu@$EC2_HOST "uptime"
                      docker pull $DOCKER_IMAGE:latest
                      docker stop my-java-app || true
                      docker rm my-java-app || true
                      docker run -d \
                        --name my-java-app \
                        -p 8081:8080 \
                        $DOCKER_IMAGE
                    EOF
                    '''
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
