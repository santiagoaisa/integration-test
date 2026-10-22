pipeline {
    agent any
    parameters {
        choice(name: 'ENVIRONMENT', choices: ['default', 'dev', 'soap'], description: 'Selecciona el entorno de pruebas')
        string(name: 'TAGS', defaultValue: '', description: 'Dejar vacío para ejecutar todas las pruebas')
    }
    stages {
        stage('Integration Tests') {
            steps {
                script {
                    def tagsOption = TAGS?.trim() ? "-Dcucumber.filter.tags='${TAGS}'" : ""
                    sh "docker run --rm -v ${WORKSPACE}:/usr/src/app -w /usr/src/app maven:3.8.8-eclipse-temurin-17 mvn clean verify -Denvironment=${ENVIRONMENT} ${tagsOption} -B -ntp"
                }
            }
            post {
                always {
                    publishHTML(
                        target: [
                            reportName           : 'Serenity Report',
                            reportDir            : 'target/site/serenity',
                            reportFiles          : 'index.html',
                            keepAll              : true,
                            alwaysLinkToLastBuild: true,
                            allowMissing         : false
                        ]
                    )
                }
            }
        }
    }
}