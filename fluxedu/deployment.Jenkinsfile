pipeline {
//     options {
//       timeout(time: 30, unit: 'MINUTES')
//     }
// add options with timeout 25 minutes

    agent 'any'
    environment {
        NEXUS_CRED = credentials("$NEXUS_CREDENTIALS")
        ARTIFACT_VERSION = sh(script: "curl -u '$NEXUS_CRED_USR:$NEXUS_CRED_PSW' -X GET 'https://nexus.dev.provalliance.com.pl/service/rest/v1/search/assets?repository=maven-releases&maven.groupId=provalliance.api&maven.artifactId=api&maven.extension=jar&sort=version' -stderr | grep '\"path\"' | head -n 1 | cut -d '/' -f4", returnStdout: true).trim()
    }

    stages {
        stage('Download') {
            steps {
                sh "curl -L -u '$NEXUS_CRED_USR:$NEXUS_CRED_PSW' -X GET 'https://nexus.dev.provalliance.com.pl/service/rest/v1/search/assets/download?repository=maven-releases&maven.groupId=provalliance.api&maven.artifactId=api&maven.extension=jar&version=$ARTIFACT_VERSION' --output application.jar"
            }
        }
        stage('Repackage') {
            steps {
                sh "unzip -o application.jar Procfile .ebextensions/* .platform/*"
                sh 'zip -d application.jar Procfile .ebextensions/* .platform/*'
                sh "find .platform/ -type f -exec chmod +x {} \\;"
                sh "zip -r application.zip application.jar .ebextensions .platform Procfile"
            }
        }
        stage('Deploy') {
						agent {
								docker {
										image 'amazon/aws-cli'
										reuseNode true
										args "--entrypoint=''"
								}
						}
            when {
              expression {
                currentBuild.result == null || currentBuild.result == 'SUCCESS'
              }
            }
            steps {
            		withAWS(region: "${params.AWS_REGION}", credentials: "${params.AWS_CREDENTIALS_ID_DEPLOYMENT_TARGET}") {
										script {
												env.VERSION_LABEL = "api-${ARTIFACT_VERSION}"
												env.S3_BUCKET = sh (
														script: "aws elasticbeanstalk create-storage-location --output text --query 'S3Bucket'",
														returnStdout: true
														).trim()
												env.S3_KEY = "${params.AWS_ELASTIC_BEANSTALK_APPLICATION_NAME}-${env.VERSION_LABEL}.zip"
										}
										s3Upload bucket: "${env.S3_BUCKET}",
												file: "application.zip",
												path: "${env.S3_KEY}"
										sh "aws elasticbeanstalk create-application-version --application-name '${params.AWS_ELASTIC_BEANSTALK_APPLICATION_NAME}' --version-label ${env.VERSION_LABEL} --source-bundle S3Bucket='${env.S3_BUCKET}',S3Key='${env.S3_KEY}'"
										sh "aws elasticbeanstalk update-environment --environment-name '${params.AWS_ELASTIC_BEANSTALK_ENVIRONMENT_NAME}' --version-label ${env.VERSION_LABEL}"
										sh "aws elasticbeanstalk wait environment-updated --application-name '${params.AWS_ELASTIC_BEANSTALK_APPLICATION_NAME}' --version-label ${env.VERSION_LABEL} --environment-names '${params.AWS_ELASTIC_BEANSTALK_ENVIRONMENT_NAME}'"
								}
            }
        }
    }

    post {
        success {
            cleanWs()
        }
    }
}