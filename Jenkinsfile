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
            defaultValue: 'auto',
            description: 'Comando Python. Use "auto" para detectar, o ruta completa ej. C:\\Python312\\python.exe'
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
        PLAYWRIGHT_TEST_RESULTS  = "${PLAYWRIGHT_DIR}/test-results"
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
                                call :resolve_python
                                if errorlevel 1 (
                                    echo ADVERTENCIA: Python no encontrado
                                ) else (
                                    echo Python detectado: %PYTHON_EXE%
                                    %PYTHON_EXE% --version
                                )
                                exit /b 0

                                :resolve_python
                                if /I not "${params.PYTHON_CMD}"=="auto" (
                                    set "PYTHON_EXE=${params.PYTHON_CMD}"
                                    exit /b 0
                                )
                                where python >nul 2>&1 && set "PYTHON_EXE=python" && exit /b 0
                                where py >nul 2>&1 && set "PYTHON_EXE=py -3" && exit /b 0
                                if exist "C:\\Program Files\\Python312\\python.exe" set "PYTHON_EXE=C:\\Program Files\\Python312\\python.exe" && exit /b 0
                                if exist "C:\\Program Files\\Python311\\python.exe" set "PYTHON_EXE=C:\\Program Files\\Python311\\python.exe" && exit /b 0
                                if exist "C:\\Program Files\\Python310\\python.exe" set "PYTHON_EXE=C:\\Program Files\\Python310\\python.exe" && exit /b 0
                                exit /b 1
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
                                    python scripts/build_allure_report.py || echo "ADVERTENCIA: Allure HTML no generado"
                                '''
                            } else {
                                bat """
                                    @echo off
                                    call :resolve_python
                                    if errorlevel 1 (
                                        echo ERROR: Python no encontrado en el agente Jenkins.
                                        echo Instala Python 3.10+ para todos los usuarios, agrega al PATH del sistema y reinicia el servicio Jenkins.
                                        echo O ejecuta con PYTHON_CMD = ruta completa a python.exe
                                        exit /b 1
                                    )
                                    echo Usando Python: %PYTHON_EXE%
                                    %PYTHON_EXE% -m venv .venv
                                    if errorlevel 1 exit /b 1
                                    call .venv\\Scripts\\activate.bat
                                    pip install --upgrade pip
                                    pip install -r requirements.txt
                                    playwright install chromium
                                    pytest
                                    if errorlevel 1 exit /b 1
                                    python scripts\\build_allure_report.py
                                    if errorlevel 1 echo ADVERTENCIA: Allure HTML no generado, revisar reports/allure-results
                                    exit /b 0

                                    :resolve_python
                                    if /I not "${params.PYTHON_CMD}"=="auto" (
                                        set "PYTHON_EXE=${params.PYTHON_CMD}"
                                        exit /b 0
                                    )
                                    where python >nul 2>&1 && set "PYTHON_EXE=python" && exit /b 0
                                    where py >nul 2>&1 && set "PYTHON_EXE=py -3" && exit /b 0
                                    if exist "C:\\Program Files\\Python312\\python.exe" set "PYTHON_EXE=C:\\Program Files\\Python312\\python.exe" && exit /b 0
                                    if exist "C:\\Program Files\\Python311\\python.exe" set "PYTHON_EXE=C:\\Program Files\\Python311\\python.exe" && exit /b 0
                                    if exist "C:\\Program Files\\Python310\\python.exe" set "PYTHON_EXE=C:\\Program Files\\Python310\\python.exe" && exit /b 0
                                    exit /b 1
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
                    junit(
                        testResults: "${env.PLAYWRIGHT_REPORTS_DIR}/junit.xml",
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

                catchError(buildResult: null, stageResult: 'UNSTABLE') {
                    publishHTML(target: [
                        allowMissing          : true,
                        alwaysLinkToLastBuild : true,
                        keepAll               : true,
                        reportDir             : "${env.PLAYWRIGHT_REPORTS_DIR}/allure-report",
                        reportFiles           : 'index.html',
                        reportName            : 'Playwright Allure Report',
                        reportTitles          : 'Allure Report'
                    ])
                }
            }
        }

        stage('Archivar evidencias') {
            steps {
                catchError(buildResult: null, stageResult: 'UNSTABLE') {
                    archiveArtifacts(
                        artifacts: "${env.SERENITY_REPORTS_DIR}/**/*,${env.SUREFIRE_REPORTS_DIR}/**/*,${env.PLAYWRIGHT_REPORTS_DIR}/**/*,${env.PLAYWRIGHT_TEST_RESULTS}/**/*",
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
            echo 'Pipeline fallido. Stage 1 (Serenity) o Stage 2 (Playwright) tuvo errores.'
            echo 'Si Serenity paso: instala Python para todos los usuarios y reinicia Jenkins, o usa PYTHON_CMD con ruta completa.'
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
