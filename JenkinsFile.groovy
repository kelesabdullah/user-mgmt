
pipeline {
    agent any
    options {
        skipStagesAfterUnstable()
    }
    environment {
        ECR_REPO_NAME = "user-mgmt"
        AWS_REGION = "eu-central-1"
        AWS_CREDENTIALS = "aws-credentials"
        FILE_NAME = "app-deployment.yaml"
        ENVR = "dev"
        ACCOUNT_ID = "XXXXXX"
    }
    stages {
        stage('Clone repository') {
            steps {
                script {
                    checkout scm
                }
            }
        }
        stage('Build') {
            steps {
                script {
                    app = docker.build(ECR_REPO_NAME)
                }
            }
        }
        stage('Test') {
            steps {
                echo 'Testing.'
            }
        }
        
        stage('Deploy') {
            steps {
                script {
                    docker.withRegistry("https://${ACCOUNT_ID}.dkr.ecr.eu-central-1.amazonaws.com","ecr:eu-central-1:${AWS_CREDENTIALS}") {
                        app.push("${env.BUILD_NUMBER}")
                    }
                }
            }
        }
         stage('Remove Local Image') {
            steps {
                script {
                    sh "docker rmi -f ${ACCOUNT_ID}.dkr.ecr.eu-central-1.amazonaws.com/${ECR_REPO_NAME}:${env.BUILD_NUMBER}"
                }
            }
        }       
    }
}