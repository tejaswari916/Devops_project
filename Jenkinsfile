pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                echo 'GitHub source code is available'
            }
        }

        stage('Build') {
            steps {
                sh './mvnw -B clean package'
            }
        }

        stage('Test') {
            steps {
                echo 'Build and test stage completed'
            }
        }
    }
}