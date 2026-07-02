"""Genera reporte Allure HTML desde reports/allure-results."""

from __future__ import annotations

import shutil
import subprocess
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
ALLURE_RESULTS = ROOT / "reports" / "allure-results"
ALLURE_REPORT = ROOT / "reports" / "allure-report"


def allure_commands(results_dir: Path, report_dir: Path) -> list[list[str]]:
    commands: list[list[str]] = []
    allure_exe = shutil.which("allure")
    npx_exe = shutil.which("npx")
    if allure_exe:
        commands.append(
            [allure_exe, "generate", str(results_dir), "-o", str(report_dir), "--clean"]
        )
    if npx_exe:
        commands.append(
            [
                npx_exe,
                "--yes",
                "allure-commandline",
                "generate",
                str(results_dir),
                "-o",
                str(report_dir),
                "--clean",
            ]
        )
    return commands


def main() -> int:
    if not ALLURE_RESULTS.exists() or not any(ALLURE_RESULTS.iterdir()):
        print("No hay resultados en reports/allure-results. Ejecuta pytest primero.")
        return 1

    ALLURE_REPORT.mkdir(parents=True, exist_ok=True)
    commands = allure_commands(ALLURE_RESULTS, ALLURE_REPORT)

    for cmd in commands:
        try:
            print(f"Ejecutando: {' '.join(cmd)}")
            result = subprocess.run(cmd, cwd=ROOT)
        except FileNotFoundError:
            continue
        if result.returncode == 0 and (ALLURE_REPORT / "index.html").exists():
            print(f"Reporte Allure: {ALLURE_REPORT / 'index.html'}")
            return 0

    print(
        "No se pudo generar Allure HTML. Instala Allure CLI o usa Node.js (npx).\n"
        "Opciones:\n"
        "  scoop install allure\n"
        "  choco install allure-commandline\n"
        "  npx --yes allure-commandline generate reports/allure-results -o reports/allure-report --clean"
    )
    return 1


if __name__ == "__main__":
    raise SystemExit(main())
