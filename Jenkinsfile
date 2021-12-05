pipeline {
    agent any
    stages {
        stage('Validate & Initialize') {
            agent any
            steps {
                checkout scm
                setJobDescription(env.JOB_NAME, jobDescription)
                rtBuildInfo(captureEnv: true) // Enable traceability (build info) capture
                script {
                    boolean valid = validateDeclarativePipeline 'Jenkinsfile'
                    if (!valid) {
                        currentBuild.result = 'FAILURE'
                        error('VALIDATION ERROR')
                    }
                }
                cleanWs cleanWhenFailure: false, cleanWhenUnstable: false
            }
        }
    }

}