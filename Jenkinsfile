#!/usr/bin/env groovy

@Library('piper-library-os') _

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
                checkout scm
            }
        }

        stage('Compile and Unit Tests') {
            steps {
                durationMeasure(script: this, measurementName: 'build_duration') {
                    sh "mvn test"

                    script {
                        junit '**/target/surefire-reports/TEST-*.xml'
                        archive 'target/*.jar'
                        step([$class: 'JacocoPublisher', execPattern: '**/target/jacoco.exec'])
                    }
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
                    }
                } catch (err) {
                    if (currentBuild.result == null) {
                        currentBuild.result = "FAILURE" // sets the ordinal as 4 and boolean to false
                    }
                    throw err
                } finally {
                    step([$class                : 'InfluxDbPublisher',
                          customData            : null,
                          customDataMap         : null,
                          customPrefix          : null,
                          customProjectName     : 'TvTrader',
                          target                : 'InfluxDB',
                          selectedTarget        : 'InfluxDB'
                    ])
                }
            }

        }
    }
}