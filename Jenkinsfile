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
        NEXUS_REPOSITORY = "maven-releases"
        NEXUS_REPOSITORYY = "docker-releases"
        NEXUS_CREDENTIAL_ID = "nexus-credentials"
        KUBECONFIG = credentials('kubeconfig-credentials-id')
        APP_NAMESPACE = "myapp"
        PROMETHEUS_URL = "http://localhost/prometheus"
        GRAFANA_URL = "http://localhost/grafana"
        GRAFANA_CREDS = credentials('grafana-admin-credentials')
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
                    env.JAVA_HOME = tool name: 'JDK 17', type: 'jdk'
                    env.PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
                    sh 'java -version'
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
                        env.JAVA_HOME = tool name: 'JDK 17', type: 'jdk'
                        env.PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
                        sh 'java -version'
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

        stage('Build Package') {
            steps {
                sh 'mvn package'
            }
        }


         stage('Push to Nexus Repository') {
            steps {
                script {
                    pom = readMavenPom file: "pom.xml"
                    pom.version = "${pom.version}-${env.BUILD_NUMBER}" // Unique version per build
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

        stage('Build Docker Image') {
            steps {
                script {
                    echo 'Building Docker image...'
                    sh 'docker build -t $DOCKER_IMAGE .'
                }
            }
        }

        stage('Push Docker Image to Nexus') {
            steps {
                script {
                    def imageName = "${DOCKER_IMAGE.split(':')[0].split('/')[-1]}" // Extracts only the image name
                    def version = "${BUILD_NUMBER}" // Unique version for each build
                    def tarFile = "${imageName}-${version}.tar"

                    // Save Docker image as a tarball file
                    echo 'Saving Docker image as tarball...'
                    sh "docker save -o ${tarFile} ${DOCKER_IMAGE}"

                    // Upload tarball to Nexus as a raw artifact
                    nexusArtifactUploader(
                        nexusVersion: 'nexus3',
                        protocol: 'http',
                        nexusUrl: '10.0.2.15:8081',
                        repository: 'docker-images-raw',
                        credentialsId: 'nexus-credentials',
                        groupId: 'com.example.docker',
                        version: version,
                        artifacts: [
                            [
                                artifactId: imageName,
                                classifier: '',
                                file: tarFile,
                                type: 'tar'
                            ]
                        ]
                    )
                }
            }
        }


        // stage('Push Docker Image to Docker Hub') {
        //     steps {
        //         script {
        //             echo 'Pushing Docker image to Docker Hub...'
        //             sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
        //             sh 'docker push $DOCKER_IMAGE'
        //         }
        //     }
        // }

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

        stage('Deploy to Kubernetes') {             
            steps {                 
                script {                     
                    sh '''                         
                        # Apply the deployment and service to the specified namespace
                        kubectl --kubeconfig=$KUBECONFIG apply -f k8s-deployment.yaml                     
                    '''                 
                }             
            }         
        }


        stage('Verify Deployment') {
            steps {
                script {
                    sh '''
                        kubectl --kubeconfig=$KUBECONFIG get deployments -n $APP_NAMESPACE
                        kubectl --kubeconfig=$KUBECONFIG get pods -n $APP_NAMESPACE
                        kubectl --kubeconfig=$KUBECONFIG get services -n $APP_NAMESPACE
                    '''
                }
            }
        }

        stage('Setup Prometheus and Grafana Config') {
            steps {
                script {
                    sh '''
                        kubectl --kubeconfig=$KUBECONFIG apply -f setup-prometheus.yaml
                        kubectl --kubeconfig=$KUBECONFIG apply -f node-exporter.yaml
                        kubectl --kubeconfig=$KUBECONFIG apply -f kube-state-metrics.yaml
                        kubectl --kubeconfig=$KUBECONFIG apply -f setup-grafana.yaml
                    '''
                }
            }
        }

        stage('Wait for Grafana') {
            steps {
                script {
                    // Wait for Grafana to be ready
                    sh '''
                        attempt_counter=0
                        max_attempts=30
                        
                        until $(curl --output /dev/null --silent --fail http://localhost:32000/api/health); do
                            if [ ${attempt_counter} -eq ${max_attempts} ];then
                                echo "Max attempts reached. Grafana is not available."
                                exit 1
                            fi
                            
                            printf '.'
                            attempt_counter=$(($attempt_counter+1))
                            sleep 5
                        done
                    '''
                }
            }
        }

        stage('Configure Grafana Datasource') {
            steps {
                script {
                    // Add Prometheus as a datasource using basic auth
                    sh '''
                        curl -X POST http://localhost:32000/api/datasources \
                        -H "Content-Type: application/json" \
                        -u "${GRAFANA_CREDS_USR}:${GRAFANA_CREDS_PSW}" \
                        -d '{
                            "name": "Prometheus",
                            "type": "prometheus",
                            "url": "http://localhost:30090",
                            "access": "proxy",
                            "isDefault": true
                        }'
                    '''
                }
            }
        }

        stage('Create Grafana Dashboard') {
            steps {
                script {
                    // Create the dashboard using basic auth
                    sh '''
                        # Save dashboard configuration to a file
                        cat > dashboard.json << 'EOF'
                        $(cat ${WORKSPACE}/grafana-dashboard.json)
        EOF
                        
                        # Create the dashboard via API with basic auth
                        curl -X POST http://localhost:32000/api/dashboards/db \
                        -H "Content-Type: application/json" \
                        -u "${GRAFANA_CREDS_USR}:${GRAFANA_CREDS_PSW}" \
                        -d @dashboard.json
                    '''
                }
            }
        }
        

        stage('Send Notification') {
            steps {
                emailext (
                    subject: "Jenkins Pipeline: ${currentBuild.currentResult}",
                    body: """
                        <p>The Jenkins pipeline for the project has ${currentBuild.currentResult}.</p>
                        <p>You can view the pipeline logs <a href="${env.BUILD_URL}">here</a>.</p>
                    """,
                    to: "${env.DEFAULT_RECIPIENTS}",
                    replyTo: "${env.REPLY_TO_LIST}",
                    recipientProviders: [[$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']]
                )
            }
        }
    }
    
    post {
        always { cleanWs() }
        failure { echo 'Monitoring setup failed! Check the logs for details.' }
        success { echo 'Monitoring setup completed successfully!' }
    }
}