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
        OWASP_REPORT_DIR = "${WORKSPACE}/dependency-check-reports"
        TRIVY_REPORT_DIR = "${WORKSPACE}/trivy-reports"
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
                    // Create directory for reports with proper permissions
                    sh """
                        mkdir -p ${OWASP_REPORT_DIR}
                        chmod -R 777 ${OWASP_REPORT_DIR}
                    """
                    
                    // Run Dependency Check with explicit paths
                    dependencyCheck(
                        additionalArguments: """
                            -o "${OWASP_REPORT_DIR}" 
                            -s "${WORKSPACE}"
                            -f "ALL"
                            --prettyPrint
                        """,
                        odcInstallation: 'OWASP-Dependency-Check'
                    )

                    // Archive the reports
                    archiveArtifacts artifacts: "${OWASP_REPORT_DIR}/*"
                }
            }
            post {
                always {
                    dependencyCheckPublisher(
                        pattern: "${OWASP_REPORT_DIR}/dependency-check-report.xml",
                        stopBuild: false
                    )
                    
                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: OWASP_REPORT_DIR,
                        reportFiles: 'dependency-check-report.html',
                        reportName: 'OWASP Dependency Check Report'
                    ])
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
                    // Create directory for Trivy reports
                    sh """
                        mkdir -p ${TRIVY_REPORT_DIR}
                        chmod -R 777 ${TRIVY_REPORT_DIR}
                    """

                    sh """
                        # Scan for vulnerabilities in the Docker image
                        trivy image --format template --template '@contrib/html.tpl' -o ${TRIVY_REPORT_DIR}/trivy-report.html $DOCKER_IMAGE
                        # Generate JSON report for processing
                        trivy image --format json -o ${TRIVY_REPORT_DIR}/trivy-report.json $DOCKER_IMAGE
                        
                        # Optional: Check for critical vulnerabilities
                        CRITICAL_VULNS=\$(cat ${TRIVY_REPORT_DIR}/trivy-report.json | jq -r '.Results[] | select(.Vulnerabilities != null) | .Vulnerabilities[] | select(.Severity == "CRITICAL") | .VulnerabilityID' | wc -l)
                        echo "Found \$CRITICAL_VULNS critical vulnerabilities"
                        
                        # Archive the reports
                        tar -czf ${TRIVY_REPORT_DIR}/trivy-reports.tar.gz -C ${TRIVY_REPORT_DIR} .
                    """

                    // Publish Trivy scan results
                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: TRIVY_REPORT_DIR,
                        reportFiles: 'trivy-report.html',
                        reportName: 'Trivy Security Report'
                    ])

                    // Archive the reports
                    archiveArtifacts artifacts: "${TRIVY_REPORT_DIR}/*"
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


        stage('Send Notification') {
            steps {
                script {
                    // Read the security reports
                    def owaspReport = ""
                    def trivyReport = ""
                    
                    try {
                        owaspReport = readFile("${OWASP_REPORT_DIR}/dependency-check-report.html")
                    } catch (Exception e) {
                        owaspReport = "OWASP report not available: ${e.getMessage()}"
                    }
                    
                    try {
                        trivyReport = readFile("${TRIVY_REPORT_DIR}/trivy-report.html")
                    } catch (Exception e) {
                        trivyReport = "Trivy report not available: ${e.getMessage()}"
                    }

                    emailext (
                        subject: "Jenkins Pipeline: ${currentBuild.currentResult} - Security Scan Results",
                        body: """
                            <h2>Pipeline Status: ${currentBuild.currentResult}</h2>
                            <p>Build URL: <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                            
                            <h3>Security Reports Summary</h3>
                            <h4>OWASP Dependency Check Report</h4>
                            <p>Full report available in Jenkins: <a href="${env.BUILD_URL}OWASP_20Dependency_20Check_20Report">View OWASP Report</a></p>
                            
                            <h4>Trivy Security Scan Report</h4>
                            <p>Full report available in Jenkins: <a href="${env.BUILD_URL}Trivy_20Security_20Report">View Trivy Report</a></p>
                            
                            <p>Please review the detailed reports in Jenkins for complete security analysis.</p>
                        """,
                        to: "${env.DEFAULT_RECIPIENTS}",
                        replyTo: "${env.REPLY_TO_LIST}",
                        recipientProviders: [[$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']],
                        attachmentsPattern: "${OWASP_REPORT_DIR}/dependency-check-report.html,${TRIVY_REPORT_DIR}/trivy-report.html"
                    )
                }
            }
        }
    }
    
    post {
        cleanup {
            cleanWs(patterns: [
                [pattern: 'dependency-check-reports/**', type: 'INCLUDE'],
                [pattern: 'trivy-reports/**', type: 'INCLUDE']
            ])
        }
        failure { 
            echo 'Pipeline failed! Check the security scan results.' 
        }
        success { 
            echo 'Pipeline completed successfully with security scans!' 
        }
    }
}