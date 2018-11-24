#!/usr/bin/env groovy

def stageTimes = [:]
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
                script {
                    startTimer()

                    checkout scm

                    stopTimer()

                    stageTimes['checkout'] = getTimerDuration()
                }
            }
        }

        stage('Compile and Unit Tests') {
            steps {
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
                        // No surefire reports available.
                    }
                }
            }

            stage('Sonarqube Analysis') {
                steps {
                    withSonarQubeEnv('Sonarqube') {
                        sh "mvn sonargraph:create-report"
                        sh "mvn sonar:sonar"
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
                    sh "mvn deploy -Dmaven.test.skip=true"
                }
            }
        }
        post {
            always {

                script {
                    try {
                        if (currentBuild.result == null) {
                            currentBuild.result = "SUCCESS" // sets the ordinal as 0 and boolean to true
                            stageTimes['buildResult'] = "SUCCESS"
                        }
                    } catch (err) {
                        if (currentBuild.result == null) {
                            currentBuild.result = "FAILURE" // sets the ordinal as 4 and boolean to false
                            stageTimes['buildResult'] = "FAILURE"
                        }
                        throw err
                    } finally {
                        step([$class           : 'InfluxDbPublisher',
                              customData       : stageTimes,
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
}

void startTimer() {
    startTime = System.currentTimeMillis();
}

void stopTimer() {
    endTime = System.currentTimeMillis();
}

long getTimerDuration() {
    return endTime - startTime
}
