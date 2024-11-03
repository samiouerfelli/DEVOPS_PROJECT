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
        TRIVY_REPORT_DIR = 'trivy-reports'
        MAX_CRITICAL_VULNS = 20 
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
                    def reportDir = "${WORKSPACE}/dependency-check-reports"
                    
                    // Debug: Show current directory and contents
                    sh """
                        echo "Current directory: \$(pwd)"
                        echo "Directory contents before cleanup:"
                        ls -la
                        
                        echo "Cleaning and creating report directory..."
                        rm -rf ${reportDir} || true
                        mkdir -p ${reportDir}
                        chmod -R 777 ${reportDir}
                        
                        echo "Maven target directory contents:"
                        ls -la target/ || echo "No target directory found"
                    """
                    
                    // Run Dependency Check with minimal configuration
                    dependencyCheck(
                        additionalArguments: """--out '${reportDir}' 
                            --scan '${WORKSPACE}' 
                            --format XML 
                            --format HTML 
                            --prettyPrint 
                            --log '${reportDir}/dependency-check.log'
                            --nvdApiKey '28b6b1bb-0c7a-4897-8217-f745efd1d1a0'""",
                        odcInstallation: 'OWASP-Dependency-Check'
                    )
                    
                    // Debug: Show generated files
                    sh """
                        echo "Report directory contents after scan:"
                        ls -la ${reportDir}/
                        
                        if [ -f ${reportDir}/dependency-check.log ]; then
                            echo "=== Dependency Check Log ==="
                            cat ${reportDir}/dependency-check.log
                            echo "==========================="
                        fi
                    """
                }
            }
            post {
                always {
                    script {
                        echo "Starting post-build actions..."
                        
                        // Check if reports exist
                        sh """
                            echo "Checking for report files..."
                            if [ -f "${WORKSPACE}/dependency-check-reports/dependency-check-report.xml" ]; then
                                echo "XML report exists"
                            else
                                echo "XML report is missing"
                            fi
                            
                            if [ -f "${WORKSPACE}/dependency-check-reports/dependency-check-report.html" ]; then
                                echo "HTML report exists"
                            else
                                echo "HTML report is missing"
                            fi
                        """
                        
                        try {
                            // Archive artifacts first
                            archiveArtifacts(
                                artifacts: 'dependency-check-reports/**/*',
                                allowEmptyArchive: true,
                                onlyIfSuccessful: false
                            )
                            
                            // Try to publish the report
                            dependencyCheckPublisher(
                                pattern: 'dependency-check-reports/dependency-check-report.xml'
                            )
                            
                            // Try to publish HTML
                            publishHTML(target: [
                                allowMissing: true,
                                alwaysLinkToLastBuild: true,
                                keepAll: true,
                                reportDir: 'dependency-check-reports',
                                reportFiles: 'dependency-check-report.html',
                                reportName: 'OWASP Dependency Check Report'
                            ])
                        } catch (Exception e) {
                            echo "Error in post actions: ${e.getMessage()}"
                            currentBuild.result = 'UNSTABLE'
                        }
                    }
                }
                failure {
                    script {
                        echo "Dependency Check stage failed. Checking for logs..."
                        sh """
                            if [ -f "${WORKSPACE}/dependency-check-reports/dependency-check.log" ]; then
                                echo "=== Full Dependency Check Log ==="
                                cat "${WORKSPACE}/dependency-check-reports/dependency-check.log"
                                echo "================================"
                            else
                                echo "No dependency check log file found"
                            fi
                        """
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

        stage('Trivy Scan') {
            steps {
                script {
                    sh """
                        mkdir -p ${TRIVY_REPORT_DIR}
                        
                        # Run Trivy scan for vulnerabilities only (skip secrets for speed)
                        trivy image \\
                            --scanners vuln \\
                            --severity HIGH,CRITICAL \\
                            --format json \\
                            --output ${TRIVY_REPORT_DIR}/report.json \\
                            $DOCKER_IMAGE

                        # Check for critical vulnerabilities and fail if exceeds threshold
                        CRITICAL_COUNT=\$(cat ${TRIVY_REPORT_DIR}/report.json | jq -r '.Results[] | select(.Vulnerabilities != null) | .Vulnerabilities[] | select(.Severity == "CRITICAL") | .VulnerabilityID' | wc -l)
                        
                        echo "Found \$CRITICAL_COUNT critical vulnerabilities"
                        
                        if [ \$CRITICAL_COUNT -gt ${MAX_CRITICAL_VULNS} ]; then
                            echo "Failed: Too many critical vulnerabilities!"
                            exit 1
                        fi
                    """
                    
                    // Archive the report
                    archiveArtifacts "${TRIVY_REPORT_DIR}/*"
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