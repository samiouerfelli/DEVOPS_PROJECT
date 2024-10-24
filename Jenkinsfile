pipeline {
    agent any

    tools {
        jdk 'JDK 17'
        maven 'Maven 3'
    }

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials-id')
        DOCKER_IMAGE = 'lookabj/myapp:latest'
        SONAR_PROJECT_KEY = 'devops_project'
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_URL = "10.0.2.15:8081"
        NEXUS_REPOSITORY = "maven-releases-rep"
        NEXUS_CREDENTIAL_ID = "nexus-credentials"
        KUBECONFIG = credentials('kubeconfig-credentials-id')
        APP_NAMESPACE = "myapp"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Maven Clean') {
            steps {
                echo 'Running mvn clean...'
                sh 'mvn clean'
            }
        }

        stage('Maven Compile') {
            steps {
                script {
                    // Ensure JDK 17 is used for compilation
                    env.JAVA_HOME = tool name: 'JDK 17', type: 'jdk'
                    env.PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
                    sh 'java -version' // Verify Java version
                    sh 'mvn compile'
                }
            }
        }

        stage('Run Tests with JaCoCo') {
            steps {
                sh 'mvn test org.jacoco:jacoco-maven-plugin:report'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    script {
                        // Ensure JDK 17 is used for SonarQube analysis
                        env.JAVA_HOME = tool name: 'JDK 17', type: 'jdk'
                        env.PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
                        sh 'java -version' // Verify Java version
                        sh """
                        mvn sonar:sonar \
                          -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                          -Dsonar.java.coveragePlugin=jacoco \
                          -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                        """
                    }
                }
            }
        }

        // stage('Quality Gate') {
        //     steps {
        //         timeout(time: 1, unit: 'MINUTES') {
        //             waitForQualityGate abortPipeline: true
        //         }
        //     }
        // }


        stage('Build Package') {
            steps {
                sh 'mvn package'
            }
        }

        stage('Push to Nexus Repository') {
            steps {
                script {
                    pom = readMavenPom file: "pom.xml"
                    filesByGlob = findFiles(glob: "target/*.${pom.packaging}")
                    echo "${filesByGlob[0].name} ${filesByGlob[0].path} ${filesByGlob[0].directory} ${filesByGlob[0].length} ${filesByGlob[0].lastModified}"
                    artifactPath = filesByGlob[0].path
                    artifactExists = fileExists artifactPath

                    if(artifactExists) {
                        echo "*** File: ${artifactPath}, group: ${pom.groupId}, packaging: ${pom.packaging}, version ${pom.version}"
                        nexusArtifactUploader(
                            nexusVersion: NEXUS_VERSION,
                            protocol: NEXUS_PROTOCOL,
                            nexusUrl: NEXUS_URL,
                            groupId: pom.groupId,
                            version: pom.version,
                            repository: NEXUS_REPOSITORY,
                            credentialsId: NEXUS_CREDENTIAL_ID,
                            artifacts: [
                                [artifactId: pom.artifactId,
                                classifier: '',
                                file: artifactPath,
                                type: pom.packaging],
                                [artifactId: pom.artifactId,
                                classifier: '',
                                file: "pom.xml",
                                type: "pom"]
                            ]
                        )
                    } else {
                        error "*** File: ${artifactPath}, could not be found"
                    }
                }
            }
        }

        stage('Build and Load Docker Image') {
            steps {
                script {
                    echo 'Building Docker image...'
                    sh '''
                        # Build the image
                        docker build -t $DOCKER_IMAGE .
                        
                        # Load the image into Kind cluster
                        kind load docker-image $DOCKER_IMAGE --name devops-cluster
                        
                        # Verify the image is loaded
                        docker exec devops-cluster-control-plane crictl images | grep myapp
                    '''
                }
            }
        }

        

        stage('Push Docker Image to Docker Hub') {
            steps {
                script {
                    echo 'Pushing Docker image to Docker Hub...'
                    sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
                    sh 'docker push $DOCKER_IMAGE'
                }
            }
        }

        stage('Test Kubernetes Access') {
            steps {
                script {
                    sh '''
                        kubectl --kubeconfig=${KUBECONFIG} get nodes
                        kubectl --kubeconfig=${KUBECONFIG} cluster-info
                    '''
                }
            }
        }

        stage('Prepare Kubernetes Namespace') {
    steps {
        script {
            try {
                // First attempt to force remove the stuck namespace using a temporary JSON file
                sh """
                    echo '{"kind":"Namespace","apiVersion":"v1","metadata":{"name":"myapp"},"spec":{"finalizers":[]}}' > temp_ns.json
                    
                    # Try to force cleanup the namespace if it exists
                    kubectl --kubeconfig=${KUBECONFIG} get namespace myapp && {
                        echo "Namespace exists, attempting cleanup..."
                        
                        # Force remove finalizers
                        kubectl --kubeconfig=${KUBECONFIG} replace --raw "/api/v1/namespaces/myapp/finalize" -f temp_ns.json || true
                        
                        # Wait a bit for cleanup
                        sleep 10
                        
                        # Force delete the namespace
                        kubectl --kubeconfig=${KUBECONFIG} delete namespace myapp --force --grace-period=0 || true
                        
                        # Wait for namespace to be fully deleted
                        for i in \$(seq 1 30); do
                            if ! kubectl --kubeconfig=${KUBECONFIG} get namespace myapp > /dev/null 2>&1; then
                                echo "Namespace successfully deleted"
                                break
                            fi
                            echo "Waiting for namespace deletion... \$i/30"
                            sleep 2
                        done
                    } || echo "Namespace doesn't exist, proceeding with creation"
                    
                    # Clean up temporary file
                    rm -f temp_ns.json
                    
                    # Create new namespace
                    echo "Creating new namespace..."
                    kubectl --kubeconfig=${KUBECONFIG} create namespace myapp
                    
                    # Wait for namespace to be active
                    for i in \$(seq 1 30); do
                        if kubectl --kubeconfig=${KUBECONFIG} get namespace myapp | grep -q Active; then
                            echo "Namespace is active"
                            break
                        fi
                        echo "Waiting for namespace to become active... \$i/30"
                        sleep 2
                    done
                """
            } catch (Exception e) {
                echo "Failed to prepare namespace: ${e.message}"
                
                // Additional diagnostic information
                sh """
                    echo "Current namespaces:"
                    kubectl --kubeconfig=${KUBECONFIG} get namespaces
                    
                    echo "Detailed namespace information:"
                    kubectl --kubeconfig=${KUBECONFIG} describe namespace myapp || true
                    
                    echo "Cluster events:"
                    kubectl --kubeconfig=${KUBECONFIG} get events --all-namespaces | grep myapp || true
                """
                
                error "Failed to prepare namespace. Check the logs above for details."
            }
        }
    }
}

        stage('Deploy to Kubernetes') {
    steps {
        script {
            try {
                sh """
                    # Show current deployments and pods
                    echo "Current deployments:"
                    kubectl --kubeconfig=${KUBECONFIG} get deployments -n myapp
                    
                    echo "Current pods:"
                    kubectl --kubeconfig=${KUBECONFIG} get pods -n myapp
                    
                    echo "Applying deployment..."
                    kubectl --kubeconfig=${KUBECONFIG} apply -f k8s-deployment.yaml
                    
                    echo "Applying service..."
                    kubectl --kubeconfig=${KUBECONFIG} apply -f k8s-service.yaml
                    
                    # Wait for deployment with detailed status
                    echo "Waiting for deployment rollout..."
                    timeout 300s bash -c '
                        while true; do
                            echo "Checking deployment status..."
                            kubectl --kubeconfig=${KUBECONFIG} get deployment myapp-deployment -n myapp -o wide
                            echo "Checking pods status..."
                            kubectl --kubeconfig=${KUBECONFIG} get pods -n myapp -o wide
                            echo "Checking pod logs..."
                            for pod in \$(kubectl --kubeconfig=${KUBECONFIG} get pods -n myapp -l app=myapp -o name); do
                                echo "Logs for \$pod:"
                                kubectl --kubeconfig=${KUBECONFIG} logs \$pod -n myapp --tail=50 || true
                            done
                            
                            READY=\$(kubectl --kubeconfig=${KUBECONFIG} get deployment myapp-deployment -n myapp -o jsonpath='{.status.readyReplicas}')
                            DESIRED=\$(kubectl --kubeconfig=${KUBECONFIG} get deployment myapp-deployment -n myapp -o jsonpath='{.status.replicas}')
                            
                            if [ "\$READY" = "\$DESIRED" ]; then
                                echo "Deployment completed successfully"
                                exit 0
                            fi
                            
                            echo "Checking for pod events..."
                            kubectl --kubeconfig=${KUBECONFIG} get events -n myapp --sort-by=.metadata.creationTimestamp
                            
                            echo "Waiting 10 seconds before next check..."
                            sleep 10
                        done
                    '
                """
            } catch (Exception e) {
                echo "Deployment failed: ${e.message}"
                sh """
                    echo "Collecting diagnostic information..."
                    
                    echo "Deployment description:"
                    kubectl --kubeconfig=${KUBECONFIG} describe deployment myapp-deployment -n myapp
                    
                    echo "Pod descriptions:"
                    kubectl --kubeconfig=${KUBECONFIG} describe pods -n myapp -l app=myapp
                    
                    echo "Recent events:"
                    kubectl --kubeconfig=${KUBECONFIG} get events -n myapp --sort-by=.metadata.creationTimestamp
                    
                    echo "Docker images in Kind cluster:"
                    docker exec devops-cluster-control-plane crictl images
                    
                    echo "Node status:"
                    kubectl --kubeconfig=${KUBECONFIG} describe nodes
                """
                error "Deployment failed. Check the logs above for details."
            }
        }
    }
}

        stage('Verify Deployment') {
            steps {
                script {
                    sh """
                        # Wait for pods to be ready
                        kubectl --kubeconfig=${KUBECONFIG} wait --for=condition=ready pods -l app=myapp -n ${APP_NAMESPACE} --timeout=300s
                        
                        # Check deployment status
                        echo "Deployment Status:"
                        kubectl --kubeconfig=${KUBECONFIG} get deployment myapp-deployment -n ${APP_NAMESPACE} -o wide
                        
                        # Check pod status
                        echo "Pod Status:"
                        kubectl --kubeconfig=${KUBECONFIG} get pods -n ${APP_NAMESPACE} -o wide
                        
                        # Check service status
                        echo "Service Status:"
                        kubectl --kubeconfig=${KUBECONFIG} get service myapp-service -n ${APP_NAMESPACE}
                        
                        # Get service endpoint
                        echo "Service Endpoint:"
                        kubectl --kubeconfig=${KUBECONFIG} get service myapp-service -n ${APP_NAMESPACE} -o jsonpath='{.status.loadBalancer.ingress[0].hostname}'
                    """
                }
            }
        }
    }
    

     post {
        success {
            echo 'Deployment successful!'
        }
        failure {
            script {
                sh """
                    echo "Deployment failed. Collecting diagnostic information..."
                    kubectl --kubeconfig=${KUBECONFIG} describe deployment myapp-deployment -n ${APP_NAMESPACE}
                    kubectl --kubeconfig=${KUBECONFIG} describe pods -l app=myapp -n ${APP_NAMESPACE}
                    kubectl --kubeconfig=${KUBECONFIG} logs -l app=myapp -n ${APP_NAMESPACE} --tail=100
                    kubectl --kubeconfig=${KUBECONFIG} get events -n ${APP_NAMESPACE} --sort-by='.lastTimestamp'
                """
            }
        }
        always {
            cleanWs()
        }
    }
}