#!/usr/bin/env groovy

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
                sh "mvn test"
            }
        }

        stage('Sonarqube Analysis') {
            steps {
                withSonarQubeEnv('Sonarqube') {
                    sh "mvn sonar:sonar -Dmaven.test.skip=true"
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
        failure {
            script {
                currentBuild.result = "FAILURE"
            }
            updateGitlabCommitStatus name: 'Test', state: 'failed'
        }
        success {
            script {
                currentBuild.result = "SUCCESS"
            }
            updateGitlabCommitStatus name: 'Test', state: 'success'
        }

        cleanup {
            withCredentials([usernamePassword(credentialsId: 'influx_workaround', usernameVariable: 'USER', passwordVariable: 'PASSWD')]) {

                script {
                    step([$class       : 'InfluxDbPublisher',
                          customData   : null,
                          customDataMap: null,
                          customPrefix : null,
                          target       : 'InfluxDB'])
                }
            }
        }
    }
}