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
                   def tagsOption = TAGS?.trim() ? "-Dcucumber.filter.tags=\"${TAGS.trim()}\"" : ""

                   // Convertimos la ruta /var/jenkins_home al volumen real del host 'jenkins_home_data'
                   def hostWorkspace = WORKSPACE.replace('/var/jenkins_home', 'jenkins_home_data')

                   sh """
                       docker run --rm \
                         -v ${hostWorkspace}:/usr/src/app \
                         -v maven-repo:/root/.m2 \
                         -w /usr/src/app \
                         maven:3.8.8-eclipse-temurin-17 \
                         mvn clean verify -Denvironment=${ENVIRONMENT} ${tagsOption} -B -ntp
                   """
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