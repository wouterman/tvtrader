#!/usr/bin/env groovy

def buildTimes = [:]
def startTime
def endTime

pipeline {
    agent any

    options {
        buildDiscarder(
                logRotator(numToKeepStr: '1')
        )
    }

    tools {
        maven 'maven 3.5.4'
        jdk 'JDK8'
    }

    triggers {
        gitlab(triggerOnPush: true, triggerOnMergeRequest: true, branchFilterType: 'All')
    }

    stages {
        stage('Checkout source code') {
            steps {
                startTimer()

                checkout scm

                stopTimer()
                script {
                    buildTimes['Checkout source code'] = getDuration()
                }
            }
        }

        stage('Compile and Unit Tests') {
            steps {
                startTimer()

                sh "mvn test"

                script {
                    try {
                        jacoco(
                                execPattern: '**/target/*.exec',
                                classPattern: '**/target/classes',
                                sourcePattern: '**/src/main/java',
                                exclusionPattern: '**/src/test*'

                        )

                        junit '**/target/surefire-reports/TEST-*.xml'
                    } catch (ignore) {
                        // No unit tests available
                    }

                    stopTimer()
                    buildTimes['Compile and Unit Tests'] = getDuration()
                }
            }
        }

        stage('Sonarqube Analysis') {
            steps {
                startTimer()

                withSonarQubeEnv('Sonarqube') {
                    sh "mvn sonar:sonar"
                }

                stopTimer()
                script {
                    buildTimes['Sonarqube Analysis'] = getDuration()
                }
            }
        }

        stage('Quality Gate') {
            agent none

            steps {
                timeout(time: 1, unit: 'HOURS') {
                    waitForQualityGate abortPipeline: true
                }
            }

        }

        stage('Repository upload') {
            steps {
                startTimer()

                sh "mvn deploy -Dmaven.test.skip=true"

                stopTimer()
                script {
                    buildTimes['Repository upload'] = getDuration()
                }
            }
        }
    }
    post {
        always {

            script {
                try {
                    if (currentBuild.result == null) {
                        currentBuild.result = "SUCCESS" // sets the ordinal as 0 and boolean to true
                        buildTimes['buildResult'] = "SUCCESS"
                    }
                } catch (err) {
                    if (currentBuild.result == null) {
                        currentBuild.result = "FAILURE" // sets the ordinal as 4 and boolean to false
                        buildTimes['buildResult'] = "FAILURE"
                    }
                    throw err
                } finally {
                    step([$class           : 'InfluxDbPublisher',
                          customData       : buildTimes,
                          customDataMap    : null,
                          customPrefix     : null,
                          customProjectName: 'TvTrader',
                          target           : 'InfluxDB',
                          selectedTarget   : 'InfluxDB'
                    ])
                }
            }

        }
    }
}

void startTimer() {
    startTime = System.currentTimeMillis();
}

void stopTimer() {
    endTime = System.currentTimeMillis();
}

long getDuration() {
    return endTime - startTime
}