from pytest_bdd import given, parsers, scenarios, then, when

from config.settings import STANDARD_PASSWORD, STANDARD_USER
from pages.inventory_page import InventoryPage
from pages.login_page import LoginPage
from utils.price_parser import is_sorted_ascending

scenarios("product_sorting.feature")


@given("que un usuario autenticado visualiza el catálogo de productos")
def authenticated_user_on_catalog(page):
    login_page = LoginPage(page)
    login_page.open()
    login_page.login(STANDARD_USER, STANDARD_PASSWORD)
    InventoryPage(page).wait_for_catalog()


@when(parsers.parse('ordena los productos por "{sort_option}"'))
def sort_products(page, sort_option):
    InventoryPage(page).sort_by(sort_option)


@then("los productos deberían mostrarse ordenados por precio ascendente")
def verify_prices_sorted_ascending(page):
    prices = InventoryPage(page).get_product_prices()
    assert len(prices) > 1, "Se esperaba al menos dos productos en el catálogo"
    assert is_sorted_ascending(prices), f"Precios no ordenados ascendentemente: {prices}"
