#!/usr/bin/env groovy

pipeline {
    agent any

    post {
        failure {
            updateGitlabCommitStatus name: 'Test', state: 'failed'
        }
        success {
            updateGitlabCommitStatus name: 'Test', state: 'success'
        }
    }

    options {
        gitLabConnection('Gitlab')
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

        stage('Compile source code') {
            steps {
                sh "mvn compile"
                echo "BRANCH: $env.gitlabBranch"
            }
        }

        stage('Unit Tests') {
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

        stage('Deploying to Tomcat') {
            steps {
                sh "mvn tomcat7:redeploy -Dmaven.test.skip=true"
            }
        }

    }
}