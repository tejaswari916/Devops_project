// ============================================================
// Declarative Jenkins Pipeline
// Inventory Management System - CI/CD Pipeline
// ============================================================

pipeline {
    agent any

    environment {
        // Docker image name - set your Docker Hub username here
        DOCKER_IMAGE      = 'your-dockerhub-username/inventory-management'
        // Docker Hub credentials stored in Jenkins Credentials Manager
        DOCKER_CREDENTIALS = 'dockerhub-credentials'
        // Kubernetes namespace
        K8S_NAMESPACE     = 'inventory-system'
    }

    tools {
        // Jenkins must have a JDK 21 tool configured under Manage Jenkins > Tools
        jdk 'JDK21'
    }

    stages {

        // ── Stage 1: Checkout ──────────────────────────────────────
        stage('1. Checkout') {
            steps {
                echo '=== Checking out source code from GitHub ==='
                checkout scm
                sh 'git log --oneline -5 || echo "Git log not available"'
            }
        }

        // ── Stage 2: Verify Environment ────────────────────────────
        stage('2. Verify Environment') {
            steps {
                echo '=== Verifying build tools ==='
                sh 'java -version'
                sh 'chmod +x mvnw && ./mvnw --version'
                sh 'docker --version || echo "Docker not available on this agent"'
            }
        }

        // ── Stage 3: Run Tests ─────────────────────────────────────
        stage('3. Test') {
            steps {
                echo '=== Running unit and integration tests ==='
                sh './mvnw clean test -Dspring.profiles.active=test'
            }
            post {
                always {
                    // Publish JUnit test results even if tests fail
                    junit 'target/surefire-reports/*.xml'
                }
                failure {
                    echo 'Tests failed. Pipeline will stop. Fix test failures before deploying.'
                }
            }
        }

        // ── Stage 4: Build JAR ─────────────────────────────────────
        stage('4. Build JAR') {
            steps {
                echo '=== Building application JAR ==='
                sh './mvnw package -DskipTests -q'
                echo '=== Build artifact: ==='
                sh 'ls -lh target/*.jar'
            }
        }

        // ── Stage 5: Docker Build ──────────────────────────────────
        stage('5. Docker Build') {
            when {
                expression { return sh(script: 'which docker', returnStatus: true) == 0 }
            }
            steps {
                script {
                    def imageTag = "${DOCKER_IMAGE}:${env.GIT_COMMIT?.take(8) ?: env.BUILD_NUMBER}"
                    env.IMAGE_TAG = imageTag
                    echo "=== Building Docker image: ${imageTag} ==="
                    sh "docker build -t ${imageTag} -t ${DOCKER_IMAGE}:latest ."
                }
            }
        }

        // ── Stage 6: Docker Push ───────────────────────────────────
        stage('6. Docker Push') {
            when {
                allOf {
                    expression { return env.IMAGE_TAG != null }
                    expression { return sh(script: 'which docker', returnStatus: true) == 0 }
                }
            }
            steps {
                withCredentials([usernamePassword(
                        credentialsId: "${DOCKER_CREDENTIALS}",
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS')]) {
                    sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                    sh "docker push ${env.IMAGE_TAG}"
                    sh "docker push ${DOCKER_IMAGE}:latest"
                    sh 'docker logout'
                }
            }
        }

        // ── Stage 7: Deploy to Kubernetes ─────────────────────────
        stage('7. Deploy to Kubernetes') {
            when {
                allOf {
                    expression { return env.IMAGE_TAG != null }
                    expression { return sh(script: 'which kubectl', returnStatus: true) == 0 }
                }
            }
            steps {
                echo "=== Deploying ${env.IMAGE_TAG} to Kubernetes ==="
                sh "kubectl apply -f k8s/ --namespace=${K8S_NAMESPACE}"
                sh "kubectl set image deployment/inventory-app inventory-app=${env.IMAGE_TAG} --namespace=${K8S_NAMESPACE}"
                sh "kubectl rollout status deployment/inventory-app --namespace=${K8S_NAMESPACE} --timeout=120s"
            }
        }

        // ── Stage 8: Verify Deployment ─────────────────────────────
        stage('8. Verify Deployment') {
            when {
                expression { return sh(script: 'which kubectl', returnStatus: true) == 0 }
            }
            steps {
                echo '=== Verifying Kubernetes pods ==='
                sh "kubectl get pods --namespace=${K8S_NAMESPACE}"
                sh "kubectl get services --namespace=${K8S_NAMESPACE}"
            }
        }
    }

    post {
        success {
            echo '\u2705 Pipeline completed successfully! Application deployed.'
        }
        failure {
            echo '\u274c Pipeline failed. Check logs above for details.'
        }
        always {
            cleanWs()
        }
    }
}
