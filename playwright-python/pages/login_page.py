from playwright.sync_api import Page, expect

from config.settings import BASE_URL, STANDARD_PASSWORD


class LoginPage:
    USERNAME = "[data-test='username']"
    PASSWORD = "[data-test='password']"
    LOGIN_BUTTON = "[data-test='login-button']"
    ERROR_MESSAGE = "[data-test='error']"

    def __init__(self, page: Page) -> None:
        self.page = page

    def open(self) -> None:
        self.page.goto(BASE_URL)

    def login(self, username: str, password: str = STANDARD_PASSWORD) -> None:
        self.page.locator(self.USERNAME).fill(username)
        self.page.locator(self.PASSWORD).fill(password)
        self.page.locator(self.LOGIN_BUTTON).click()

    def error_message(self) -> str:
        return self.page.locator(self.ERROR_MESSAGE).inner_text()

    def assert_error_visible(self) -> None:
        expect(self.page.locator(self.ERROR_MESSAGE)).to_be_visible()
