pipeline {
    agent { docker { image 'gradle:jdk17' } }
    stages {
        stage('build') {
            steps {
                sh 'gradle check'
            }
        }
    }
    post{
        always{
            junit 'build/test-results/**/*.xml'
        }
    }
}