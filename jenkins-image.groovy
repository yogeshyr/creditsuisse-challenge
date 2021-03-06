String jobDescription =
    '<p>This is test job<p>'

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
                    boolean valid = validateDeclarativePipeline 'jenkins-image.groovy'
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