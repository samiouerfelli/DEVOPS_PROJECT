pipeline {
    agent any

    tools {
        jdk 'JDK 17'
        maven 'Maven 3'
        'dependency-check' 'OWASP-Dependency-Check'
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
        GRAFANA_CREDS = credentials('grafana-admin-credentials')
        DEPENDENCY_CHECK_DIR = "${WORKSPACE}/dependency-check-reports"
        
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

        stage('OWASP Dependency Check') {
    steps {
        script {
            // Create directory for reports
            sh '''
                mkdir -p ${WORKSPACE}/dependency-check-reports
                chmod -R 777 ${WORKSPACE}/dependency-check-reports
            '''
            
            // Run Dependency Check
            dependencyCheck(
                additionalArguments: '''
                    --out "${WORKSPACE}/dependency-check-reports" \
                    --scan "${WORKSPACE}/target/" \
                    --format "ALL" \
                    --prettyPrint
                ''',
                odcInstallation: 'OWASP-Dependency-Check'
            )
        }
    }
    post {
        always {
            // Publish results
            dependencyCheckPublisher(
                pattern: '**/dependency-check-reports/dependency-check-report.xml'
            )
            
            // Publish HTML report
            publishHTML([
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'dependency-check-reports',
                reportFiles: 'dependency-check-report.html',
                reportName: 'OWASP Dependency Check Report'
            ])
        }
        success {
            echo 'OWASP Dependency Check completed successfully.'
        }
        failure {
            echo 'OWASP Dependency Check failed. Check the report for details.'
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

        stage('Trivy Scan') {
            steps {
                script {
                    sh '''
                        # Scan for vulnerabilities in the Docker image
                        trivy image --format template --template '@contrib/html.tpl' -o trivy-report.html $DOCKER_IMAGE
                        # Also generate JSON report for processing
                        trivy image --format json -o trivy-report.json $DOCKER_IMAGE
                        
                        # Optional: Fail build if critical vulnerabilities found
                        CRITICAL_VULNS=$(cat trivy-report.json | jq -r '.Results[] | select(.Vulnerabilities != null) | .Vulnerabilities[] | select(.Severity == "CRITICAL") | .VulnerabilityID' | wc -l)
                        if [ $CRITICAL_VULNS -gt 0 ]; then
                            echo "Found $CRITICAL_VULNS critical vulnerabilities"
                            exit 1
                        fi
                    '''

                    // Publish Trivy scan results
                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: './',
                        reportFiles: 'trivy-report.html',
                        reportName: 'Trivy Security Report'
                    ])
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
                    sh '''
                        # Wait for Grafana to be ready
                        echo "Waiting for Grafana to be ready..."
                        until curl -s -u "${GRAFANA_CREDS_USR}:${GRAFANA_CREDS_PSW}" http://localhost:32000/api/health; do
                            sleep 5
                        done

                        # Check if Prometheus datasource exists
                        DATASOURCE_ID=$(curl -s -u "${GRAFANA_CREDS_USR}:${GRAFANA_CREDS_PSW}" \
                            http://localhost:32000/api/datasources/name/Prometheus | grep -o '"id":[0-9]*' | cut -d':' -f2)

                        if [ ! -z "$DATASOURCE_ID" ]; then
                            # Update existing datasource
                            echo "Updating existing Prometheus datasource..."
                            curl -X PUT http://localhost:32000/api/datasources/$DATASOURCE_ID \
                            -H "Content-Type: application/json" \
                            -u "${GRAFANA_CREDS_USR}:${GRAFANA_CREDS_PSW}" \
                            -d '{
                                "name": "Prometheus",
                                "type": "prometheus",
                                "url": "http://prometheus.monitoring.svc.cluster.local:9090",
                                "access": "proxy",
                                "isDefault": true,
                                "jsonData": {
                                    "timeInterval": "15s",
                                    "queryTimeout": "60s",
                                    "httpMethod": "POST"
                                },
                                "secureJsonData": {},
                                "readOnly": false
                            }'
                        else
                            # Create new datasource
                            echo "Creating new Prometheus datasource..."
                            curl -X POST http://localhost:32000/api/datasources \
                            -H "Content-Type: application/json" \
                            -u "${GRAFANA_CREDS_USR}:${GRAFANA_CREDS_PSW}" \
                            -d '{
                                "name": "Prometheus",
                                "type": "prometheus",
                                "url": "http://prometheus.monitoring.svc.cluster.local:9090",
                                "access": "proxy",
                                "isDefault": true,
                                "jsonData": {
                                    "timeInterval": "15s",
                                    "queryTimeout": "60s",
                                    "httpMethod": "POST"
                                },
                                "secureJsonData": {},
                                "readOnly": false
                            }'
                        fi

                        # Verify datasource connection
                        echo "Verifying Prometheus datasource connection..."
                        curl -s -u "${GRAFANA_CREDS_USR}:${GRAFANA_CREDS_PSW}" \
                            http://localhost:32000/api/datasources/proxy/1/api/v1/query?query=up
                    '''
                }
            }
        }

        stage('Create Grafana Dashboard') {
            steps {
                script {
                    sh '''
                        # Create or update the dashboard via API with basic auth
                        curl -X POST http://localhost:32000/api/dashboards/db \
                        -H "Content-Type: application/json" \
                        -u "${GRAFANA_CREDS_USR}:${GRAFANA_CREDS_PSW}" \
                        -d '{
                            "dashboard": {
                                "id": null,
                                "title": "Kubernetes Cluster Monitoring",
                                "tags": ["kubernetes", "monitoring"],
                                "timezone": "browser",
                                "refresh": "10s",
                                "panels": [
                                    {
                                        "title": "Pod Restart Count",
                                        "type": "timeseries",
                                        "gridPos": {
                                            "h": 8,
                                            "w": 12,
                                            "x": 0,
                                            "y": 0
                                        },
                                        "targets": [
                                            {
                                                "expr": "sum(kube_pod_container_status_restarts_total) by (pod)",
                                                "legendFormat": "{{pod}}",
                                                "interval": "",
                                                "exemplar": true
                                            }
                                        ],
                                        "options": {
                                            "tooltip": {
                                                "mode": "multi",
                                                "sort": "desc"
                                            }
                                        }
                                    },
                                    {
                                        "title": "Network Receive Rate",
                                        "type": "timeseries",
                                        "gridPos": {
                                            "h": 8,
                                            "w": 12,
                                            "x": 12,
                                            "y": 0
                                        },
                                        "targets": [
                                            {
                                                "expr": "rate(node_network_receive_bytes_total[5m])",
                                                "legendFormat": "{{device}}",
                                                "interval": "",
                                                "exemplar": true
                                            }
                                        ],
                                        "options": {
                                            "tooltip": {
                                                "mode": "multi",
                                                "sort": "desc"
                                            }
                                        }
                                    },
                                    {
                                        "title": "Pod Status by Phase",
                                        "type": "piechart",
                                        "gridPos": {
                                            "h": 8,
                                            "w": 12,
                                            "x": 0,
                                            "y": 8
                                        },
                                        "targets": [
                                            {
                                                "expr": "sum by (phase) (kube_pod_status_phase)",
                                                "legendFormat": "{{phase}}",
                                                "interval": "",
                                                "exemplar": true
                                            }
                                        ],
                                        "options": {
                                            "legend": {
                                                "displayMode": "table",
                                                "placement": "right",
                                                "values": ["value"]
                                            }
                                        }
                                    }
                                ]
                            },
                            "overwrite": true,
                            "message": "Updated by Jenkins Pipeline"
                        }'
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
        cleanup {
            cleanWs(patterns: [[pattern: 'dependency-check-reports/**', type: 'INCLUDE']])
        }
        failure { 
            echo 'Pipeline failed! Check the security scan results.' 
        }
        success { 
            echo 'Pipeline completed successfully with security scans!' 
        }
    }
}