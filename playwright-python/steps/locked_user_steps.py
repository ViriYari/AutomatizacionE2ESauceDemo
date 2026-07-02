from pytest_bdd import given, parsers, scenarios, then, when

from config.settings import LOCKED_OUT_ERROR, LOCKED_OUT_USER, STANDARD_PASSWORD
from pages.inventory_page import InventoryPage
from pages.login_page import LoginPage

scenarios("locked_user.feature")


@given("que el usuario está en la página de inicio de sesión de Sauce Demo")
def open_login_page(page):
    LoginPage(page).open()


@when(parsers.parse('intenta iniciar sesión con el usuario bloqueado "{username}"'))
def login_with_locked_user(page, username):
    LoginPage(page).login(username, STANDARD_PASSWORD)


@then("debería ver un mensaje de error indicando que el usuario está bloqueado")
def verify_locked_out_error(page):
    login_page = LoginPage(page)
    login_page.assert_error_visible()
    assert LOCKED_OUT_ERROR in login_page.error_message()


@then("no debería acceder al catálogo de productos")
def verify_catalog_not_visible(page):
    assert not InventoryPage(page).is_catalog_visible()
