# Instalación inicial de Playwright (Windows)
$ErrorActionPreference = "Stop"

Set-Location $PSScriptRoot

if (-not (Test-Path ".venv")) {
    python -m venv .venv
}

& .\.venv\Scripts\Activate.ps1
pip install --upgrade pip
pip install -r requirements.txt
python -m playwright install chromium

Write-Host ""
Write-Host "Listo. Ejecuta: pytest" -ForegroundColor Green
