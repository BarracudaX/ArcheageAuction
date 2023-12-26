pipeline {
    agent { docker { image 'gradle:jdk17' } }
    stages {
        stage('build'){
            sh './gradlew build'
        }
        stage('test') {
            steps {
                sh './gradlew check'
            }
        }
    }
    post{
        always{
            archiveArtifacts 'build/libs/**/*.jar'
            junit 'build/test-results/**/*.xml'
        }
    }
}