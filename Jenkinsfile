pipeline {
    environment {
        registry = "yogeshy01/jenkins"
        registryCredential = 'dockerhub'
        dockerImage = ''
    }
    agent any
    stages { 
        stage('Validate pipeline') {
            steps {
                checkout([$class: 'GitSCM',
                        branches                         :[[name: '*/main']],
                        doGenerateSubmoduleConfigurations: false,
                        userRemoteConfigs                : [[credentialsId: 'github-yogeshyr',url: 'git@github.com:yogeshyr/creditsuisse-challenge.git' ]]
                ])
            }
        }
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