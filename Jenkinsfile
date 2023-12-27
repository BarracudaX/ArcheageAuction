pipeline {
    agent any
    stages {
        stage('build'){
            steps{
                sh './gradlew build -x test'
            }
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