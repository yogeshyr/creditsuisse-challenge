pipeline {
    environment {
        registry = "yogeshy01/jenkins"
        registryCredential = 'dockerhub'
        dockerImage = ''
    }
    agent any
    stages { 
        stage('Build image') {
            steps {
                script {
                    dockerImage = docker.build registry + ":$BUILD_NUMBER"
                }
            } 
        }
        stage('Push Image'){
            steps {
                script {
                    docker.withRegistry( '', registryCredential ) { 
                        dockerImage.push() 
                    }
                }
            }
        }
    }
}