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
                        userRemoteConfigs                : [[credentialsId: '4ed5cf5a-2058-4347-bdbd-2062ebfbba70',url: 'git@github.com:yogeshyr/creditsuisse-challenge.git' ]]
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
        stage('Deploy to AWS ECS'){
            steps {
                withAWS(credentials: 'awscreds', region: 'us-west-2') {
                    sh "aws s3 ls"
                }
            }
        }
    }
}