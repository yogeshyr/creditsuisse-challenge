pipeline {
    environment {
        registry = "yogeshy01/jenkins"
        registryCredential = 'dockerhub'
        dockerImage = ''
    }
    agent any
    stages {
        stage('Cloning Git') { 
            steps { 
                checkout scm
            }
        } 
    }
}