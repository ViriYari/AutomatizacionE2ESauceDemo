from pathlib import Path

import pytest
from playwright.sync_api import Page

REPORTS_DIR = Path(__file__).resolve().parent / "reports"
SCREENSHOTS_DIR = REPORTS_DIR / "screenshots"


@pytest.fixture(scope="session", autouse=True)
def create_report_dirs() -> None:
    REPORTS_DIR.mkdir(parents=True, exist_ok=True)
    SCREENSHOTS_DIR.mkdir(parents=True, exist_ok=True)


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
