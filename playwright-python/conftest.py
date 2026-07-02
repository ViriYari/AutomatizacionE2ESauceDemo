import shutil
import subprocess
import sys
from pathlib import Path

import pytest
from playwright.sync_api import Page

REPORTS_DIR = Path(__file__).resolve().parent / "reports"
SCREENSHOTS_DIR = REPORTS_DIR / "screenshots"
ALLURE_RESULTS_DIR = REPORTS_DIR / "allure-results"
ALLURE_REPORT_DIR = REPORTS_DIR / "allure-report"
TEST_RESULTS_DIR = Path(__file__).resolve().parent / "test-results"


def pytest_sessionstart(session) -> None:
    """Descarga Chromium si falta (equivalente a: python -m playwright install chromium)."""
    result = subprocess.run(
        [sys.executable, "-m", "playwright", "install", "chromium"],
        capture_output=True,
        text=True,
    )
    if result.returncode != 0:
        message = result.stderr.strip() or result.stdout.strip() or "error desconocido"
        pytest.exit(f"No se pudo instalar Chromium para Playwright.\n{message}", returncode=1)


@pytest.fixture(scope="session", autouse=True)
def create_report_dirs() -> None:
    REPORTS_DIR.mkdir(parents=True, exist_ok=True)
    SCREENSHOTS_DIR.mkdir(parents=True, exist_ok=True)
    ALLURE_RESULTS_DIR.mkdir(parents=True, exist_ok=True)
    ALLURE_REPORT_DIR.mkdir(parents=True, exist_ok=True)
    TEST_RESULTS_DIR.mkdir(parents=True, exist_ok=True)


def generate_allure_html() -> None:
    """Genera reporte Allure HTML desde reports/allure-results si hay CLI disponible."""
    if not ALLURE_RESULTS_DIR.exists() or not any(ALLURE_RESULTS_DIR.iterdir()):
        return

    ALLURE_REPORT_DIR.mkdir(parents=True, exist_ok=True)
    commands = [
        ["allure", "generate", str(ALLURE_RESULTS_DIR), "-o", str(ALLURE_REPORT_DIR), "--clean"],
        [
            "npx",
            "--yes",
            "allure-commandline",
            "generate",
            str(ALLURE_RESULTS_DIR),
            "-o",
            str(ALLURE_REPORT_DIR),
            "--clean",
        ],
    ]
    for cmd in commands:
        if cmd[0] != "npx" and not shutil.which(cmd[0]):
            continue
        if cmd[0] == "npx" and not shutil.which("npx"):
            continue
        try:
            result = subprocess.run(
                cmd,
                capture_output=True,
                text=True,
                cwd=Path(__file__).resolve().parent,
            )
        except FileNotFoundError:
            continue
        if result.returncode == 0 and (ALLURE_REPORT_DIR / "index.html").exists():
            return


def pytest_sessionfinish(session, exitstatus) -> None:
    generate_allure_html()


@pytest.fixture(scope="session")
def browser_type_launch_args() -> dict:
    return {
        "headless": True,
        "args": ["--no-sandbox", "--disable-dev-shm-usage"],
    }


@pytest.hookimpl(tryfirst=True, hookwrapper=True)
def pytest_runtest_makereport(item, call):
    outcome = yield
    report = outcome.get_result()

    if report.when == "call" and report.failed:
        page: Page | None = item.funcargs.get("page")
        if page is not None:
            screenshot_path = SCREENSHOTS_DIR / f"{item.name}.png"
            page.screenshot(path=str(screenshot_path), full_page=True)
