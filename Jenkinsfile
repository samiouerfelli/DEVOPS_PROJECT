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
        GRAFANA_URL = 'http://localhost:3000'
        PROMETHEUS_URL = 'http://localhost:9090'
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

        stage('Tag and Push Docker Image to Nexus') {
    steps {
        script {
            // Save image details
            def imageName = DOCKER_IMAGE.split(':')[0]
            def imageTag = BUILD_NUMBER
            def tarFileName = "docker-image-${BUILD_NUMBER}.tar"
            
            // Add Maven settings for deployment
            def settingsXml = """
<settings>
    <servers>
        <server>
            <id>nexus</id>
            <username>${NEXUS_USER}</username>
            <password>${NEXUS_PASS}</password>
        </server>
    </servers>
</settings>"""

            // Create pom.xml for deployment
            def pomXml = """
<project>
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>docker-image</artifactId>
    <version>${BUILD_NUMBER}</version>
    <packaging>tar</packaging>
    
    <distributionManagement>
        <repository>
            <id>nexus</id>
            <url>http://${NEXUS_URL}/repository/${NEXUS_REPOSITORYY}</url>
        </repository>
    </distributionManagement>
    
    <build>
        <extensions>
            <extension>
                <groupId>org.apache.maven.wagon</groupId>
                <artifactId>wagon-http</artifactId>
                <version>3.5.3</version>
            </extension>
        </extensions>
    </build>
</project>"""
            
            withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
                try {
                    // Export Docker image to tar
                    sh "docker save ${DOCKER_IMAGE} -o ${tarFileName}"
                    
                    // Write temporary Maven files
                    writeFile file: 'settings.xml', text: settingsXml
                    writeFile file: 'pom.xml', text: pomXml
                    
                    // Create directory structure
                    sh """
                        mkdir -p target/docker-layout
                        mv ${tarFileName} target/docker-layout/
                        cd target/docker-layout
                        tar -czf ../docker-image-${BUILD_NUMBER}.tar.gz *
                    """
                    
                    // Deploy to Nexus using Maven
                    sh """
                        mvn deploy:deploy-file \
                        -Durl=http://${NEXUS_URL}/repository/${NEXUS_REPOSITORYY} \
                        -DrepositoryId=nexus \
                        -Dfile=target/docker-image-${BUILD_NUMBER}.tar.gz \
                        -DpomFile=pom.xml \
                        -Dpackaging=tar.gz \
                        -DgeneratePom=false \
                        -s settings.xml \
                        -DgroupId=com.example \
                        -DartifactId=docker-image \
                        -Dversion=${BUILD_NUMBER} \
                        -Dclassifier=docker
                    """
                    
                    // Verify upload
                    def verifyCmd = """
                        curl -s -f -u '${NEXUS_USER}:${NEXUS_PASS}' \
                        'http://${NEXUS_URL}/service/rest/v1/search?repository=${NEXUS_REPOSITORYY}&maven.groupId=com.example&maven.artifactId=docker-image&maven.version=${BUILD_NUMBER}'
                    """
                    
                    def verifyResponse = sh(script: verifyCmd, returnStatus: true)
                    if (verifyResponse == 0) {
                        echo "Successfully verified upload to Nexus"
                    } else {
                        error "Failed to verify upload in Nexus"
                    }
                    
                } catch (Exception e) {
                    error "Failed to upload Docker image: ${e.getMessage()}"
                } finally {
                    // Cleanup
                    sh """
                        rm -f settings.xml pom.xml ${tarFileName}
                        rm -rf target
                    """
                }
            }
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
                        # Apply the deployment and service
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

        stage('Setup Kubernetes Namespace and RBAC') {
            steps {
                script {
                    sh 'kubectl --kubeconfig=$KUBECONFIG apply -f prometheus-rbac.yaml'
                }
            }
        }
        
        stage('Setup Prometheus DataSource in Grafana') {
            steps {
                script {
                    def datasourceJson = """
                    {
                        "name": "Prometheus",
                        "type": "prometheus",
                        "url": "${PROMETHEUS_URL}",
                        "access": "proxy",
                        "isDefault": true,
                        "jsonData": {
                            "httpMethod": "GET",
                            "timeInterval": "5s"
                        }
                    }
                    """
                    
                    def response = sh(script: """
                        curl -X POST "${GRAFANA_URL}/api/datasources" \
                            -H 'Content-Type: application/json' \
                            -u "${GRAFANA_CREDS_USR}:${GRAFANA_CREDS_PSW}" \
                            -d '${datasourceJson.trim()}' \
                            || true
                    """, returnStatus: true)
                    
                    echo "Datasource setup completed with status: ${response}"
                }
            }
        }

        stage('Create Dashboard in Grafana') {
            steps {
                script {
                    def dashboardJson = """
                    {
                        "dashboard": {
                            "id": null,
                            "uid": "simple-dashboard",
                            "title": "Application Monitoring Dashboard",
                            "tags": ["kubernetes", "prometheus"],
                            "timezone": "browser",
                            "schemaVersion": 16,
                            "version": 1,
                            "refresh": "5s",
                            "panels": [
                                {
                                    "title": "Container CPU Usage",
                                    "type": "timeseries",
                                    "datasource": {
                                        "type": "prometheus",
                                        "uid": "Prometheus"
                                    },
                                    "targets": [
                                        {
                                            "expr": "sum(rate(container_cpu_usage_seconds_total[1m])) by (pod)",
                                            "refId": "A",
                                            "legendFormat": "{{pod}}"
                                        }
                                    ],
                                    "gridPos": { "x": 0, "y": 0, "w": 12, "h": 8 }
                                },
                                {
                                    "title": "Container Memory Usage",
                                    "type": "timeseries",
                                    "datasource": {
                                        "type": "prometheus",
                                        "uid": "Prometheus"
                                    },
                                    "targets": [
                                        {
                                            "expr": "sum(container_memory_working_set_bytes) by (pod)",
                                            "refId": "A",
                                            "legendFormat": "{{pod}}"
                                        }
                                    ],
                                    "fieldConfig": {
                                        "defaults": {
                                            "unit": "bytes"
                                        }
                                    },
                                    "gridPos": { "x": 0, "y": 8, "w": 12, "h": 8 }
                                },
                                {
                                    "title": "Available Metrics",
                                    "type": "stat",
                                    "datasource": {
                                        "type": "prometheus",
                                        "uid": "Prometheus"
                                    },
                                    "targets": [
                                        {
                                            "expr": "count(up)",
                                            "refId": "A"
                                        }
                                    ],
                                    "gridPos": { "x": 0, "y": 16, "w": 12, "h": 4 }
                                }
                            ]
                        },
                        "overwrite": true,
                        "message": "Updated by Jenkins Pipeline"
                    }
                    """
                    def response = sh(script: """
                        curl -X POST "${GRAFANA_URL}/api/dashboards/db" \
                            -H 'Content-Type: application/json' \
                            -u "${GRAFANA_CREDS_USR}:${GRAFANA_CREDS_PSW}" \
                            -d '${dashboardJson.trim()}'
                    """, returnStatus: true)
                    
                    if (response != 0) {
                        error "Failed to create dashboard. Exit code: ${response}"
                    }
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