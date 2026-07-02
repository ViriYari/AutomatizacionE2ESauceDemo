/*
 * Pipeline E2E — Sauce Demo
 * Reto 2: Stage 1 Serenity (Java) + Stage 2 Playwright (Python)
 *
 * Jenkins — configuración requerida (Windows):
 *   1. Manage Jenkins → Global Tool Configuration → Maven → Add "Maven-3.9"
 *   2. Instalar Python 3.10+ y marcar "Add to PATH" (reiniciar servicio Jenkins)
 *   3. Job Pipeline: Branch Specifier main, Script Path: Jenkinsfile
 *
 * Plugins: Pipeline, Git, HTML Publisher, JUnit, Workspace Cleanup
 */

pipeline {
    agent any

    tools {
        maven 'Maven-3.9'
    }

    options {
        timestamps()
        timeout(time: 45, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '15', artifactNumToKeepStr: '10'))
        disableConcurrentBuilds()
    }

    parameters {
        string(
            name: 'CUCUMBER_TAGS',
            defaultValue: '',
            description: 'Tags Cucumber opcionales para Serenity. Ejemplo: @Login. Vacío = todos.'
        )
        booleanParam(
            name: 'HEADLESS',
            defaultValue: true,
            description: 'Ejecutar navegadores en modo headless.'
        )
        string(
            name: 'PYTHON_CMD',
            defaultValue: 'py -3',
            description: 'Comando Python en Windows (ej. py -3, python, C:\\Python311\\python.exe).'
        )
        booleanParam(
            name: 'SKIP_CLEANUP',
            defaultValue: false,
            description: 'Conservar el workspace al finalizar.'
        )
    }

    environment {
        SERENITY_DIR           = 'serenity-bdd-java'
        PLAYWRIGHT_DIR         = 'playwright-python'
        SERENITY_REPORTS_DIR   = "${SERENITY_DIR}/target/site/serenity"
        SUREFIRE_REPORTS_DIR   = "${SERENITY_DIR}/target/surefire-reports"
        PLAYWRIGHT_REPORTS_DIR = "${PLAYWRIGHT_DIR}/reports"
        MAVEN_OPTS             = '-Xmx2048m -XX:MaxMetaspaceSize=512m'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Verificar entorno') {
            steps {
                catchError(buildResult: null, stageResult: 'UNSTABLE') {
                    script {
                        if (isUnix()) {
                            sh '''
                                echo "=== Verificando entorno (Linux/macOS) ==="
                                java -version || echo "ADVERTENCIA: Java no encontrado"
                                mvn -version || echo "ADVERTENCIA: Maven no encontrado"
                                python3 --version || python --version || echo "ADVERTENCIA: Python no encontrado"
                            '''
                        } else {
                            bat """
                                @echo off
                                echo === Verificando entorno (Windows) ===
                                java -version || echo ADVERTENCIA: Java no encontrado
                                call mvn -version || echo ADVERTENCIA: Maven no encontrado
                                ${params.PYTHON_CMD} --version || echo ADVERTENCIA: Python no encontrado
                                exit /b 0
                            """
                        }
                    }
                }
            }
        }

        stage('Stage 1: Ejecución Serenity') {
            steps {
                script {
                    def cucumberTags = params.CUCUMBER_TAGS?.trim()
                    def environmentFlag = params.HEADLESS ? '-Denvironment=ci' : ''
                    def tagsFlag = cucumberTags ? "-Dcucumber.filter.tags=${cucumberTags}" : ''

                    catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
                        dir(env.SERENITY_DIR) {
                            if (isUnix()) {
                                sh """
                                    set -e
                                    mvn --batch-mode dependency:resolve dependency:resolve-plugins
                                    mvn --batch-mode clean test \\
                                        ${environmentFlag} \\
                                        ${tagsFlag} \\
                                        -Dwebdriver.driver=chrome \\
                                        -Dsurefire.printSummary=true
                                """
                            } else {
                                bat """
                                    call mvn --batch-mode dependency:resolve dependency:resolve-plugins
                                    if errorlevel 1 exit /b 1
                                    call mvn --batch-mode clean test ^
                                        ${environmentFlag} ^
                                        ${tagsFlag} ^
                                        -Dwebdriver.driver=chrome ^
                                        -Dsurefire.printSummary=true
                                    if errorlevel 1 exit /b 1
                                """
                            }
                        }
                    }
                }
            }
            post {
                always {
                    catchError(buildResult: null, stageResult: 'UNSTABLE') {
                        dir(env.SERENITY_DIR) {
                            script {
                                if (isUnix()) {
                                    sh '''
                                        if [ ! -f "target/site/serenity/index.html" ]; then
                                            mvn --batch-mode serenity:aggregate || true
                                        fi
                                    '''
                                } else {
                                    bat '''
                                        if not exist "target\\site\\serenity\\index.html" (
                                            call mvn --batch-mode serenity:aggregate
                                        )
                                    '''
                                }
                            }
                        }
                    }
                }
            }
        }

        stage('Stage 2: Ejecución Playwright') {
            steps {
                catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
                    dir(env.PLAYWRIGHT_DIR) {
                        script {
                            if (isUnix()) {
                                sh '''
                                    set -e
                                    python3 -m venv .venv || python -m venv .venv
                                    . .venv/bin/activate
                                    pip install --upgrade pip
                                    pip install -r requirements.txt
                                    playwright install chromium
                                    pytest
                                '''
                            } else {
                                bat """
                                    ${params.PYTHON_CMD} -m venv .venv
                                    if errorlevel 1 exit /b 1
                                    call .venv\\Scripts\\activate.bat
                                    pip install --upgrade pip
                                    pip install -r requirements.txt
                                    playwright install chromium
                                    pytest
                                    if errorlevel 1 exit /b 1
                                """
                            }
                        }
                    }
                }
            }
        }

        stage('Publicar reportes') {
            steps {
                catchError(buildResult: null, stageResult: 'UNSTABLE') {
                    junit(
                        testResults: "${env.SUREFIRE_REPORTS_DIR}/*.xml",
                        allowEmptyResults: true,
                        skipPublishingChecks: true
                    )
                }

                catchError(buildResult: null, stageResult: 'UNSTABLE') {
                    publishHTML(target: [
                        allowMissing          : true,
                        alwaysLinkToLastBuild : true,
                        keepAll               : true,
                        reportDir             : env.SERENITY_REPORTS_DIR,
                        reportFiles           : 'index.html',
                        reportName            : 'Serenity BDD Report',
                        reportTitles          : 'Serenity Report'
                    ])
                }

                catchError(buildResult: null, stageResult: 'UNSTABLE') {
                    publishHTML(target: [
                        allowMissing          : true,
                        alwaysLinkToLastBuild : true,
                        keepAll               : true,
                        reportDir             : env.PLAYWRIGHT_REPORTS_DIR,
                        reportFiles           : 'report.html',
                        reportName            : 'Playwright Report',
                        reportTitles          : 'Playwright Report'
                    ])
                }
            }
        }

        stage('Archivar evidencias') {
            steps {
                catchError(buildResult: null, stageResult: 'UNSTABLE') {
                    archiveArtifacts(
                        artifacts: "${env.SERENITY_REPORTS_DIR}/**/*,${env.SUREFIRE_REPORTS_DIR}/**/*,${env.PLAYWRIGHT_REPORTS_DIR}/**/*",
                        fingerprint: true,
                        allowEmptyArchive: true,
                        onlyIfSuccessful: false
                    )
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline completado. Revisa Serenity BDD Report y Playwright Report.'
        }

        failure {
            echo 'Pipeline fallido. Verifica Maven (Global Tool Configuration) y Python en el agente Jenkins.'
        }

        always {
            script {
                if (fileExists("${env.SERENITY_REPORTS_DIR}/index.html")) {
                    echo "Reporte Serenity: ${env.SERENITY_REPORTS_DIR}/index.html"
                } else {
                    echo 'Advertencia: no se generó index.html de Serenity.'
                }

                if (fileExists("${env.PLAYWRIGHT_REPORTS_DIR}/report.html")) {
                    echo "Reporte Playwright: ${env.PLAYWRIGHT_REPORTS_DIR}/report.html"
                } else {
                    echo 'Advertencia: no se generó report.html de Playwright.'
                }
            }

            cleanWs(
                cleanWhenSuccess: !params.SKIP_CLEANUP,
                cleanWhenFailure: !params.SKIP_CLEANUP,
                cleanWhenUnstable: !params.SKIP_CLEANUP,
                cleanWhenAborted: true,
                deleteDirs: true,
                disableDeferredWipeout: true,
                notFailBuild: true
            )
        }
    }
}
