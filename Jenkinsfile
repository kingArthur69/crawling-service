pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        stage('SonarQube') {
            steps {
                sh 'mvn sonar:sonar'
            }
            steps {
                timeout(time: 3, unit: 'MINUTES') {
                waitForQualityGate abortPipeline: true
                }
            }
        }
    }
}

