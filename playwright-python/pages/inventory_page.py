from playwright.sync_api import Page, expect

from config.settings import SORT_OPTIONS
from utils.price_parser import parse_price


class InventoryPage:
    CATALOG_TITLE = "[data-test='title']"
    INVENTORY_LIST = "[data-test='inventory-list']"
    SORT_CONTAINER = "[data-test='product-sort-container']"
    ITEM_PRICE = "[data-test='inventory-item-price']"

    def __init__(self, page: Page) -> None:
        self.page = page

    def wait_for_catalog(self) -> None:
        expect(self.page.locator(self.CATALOG_TITLE)).to_have_text("Products")
        expect(self.page.locator(self.INVENTORY_LIST)).to_be_visible()

    def is_catalog_visible(self) -> bool:
        title = self.page.locator(self.CATALOG_TITLE)
        return title.is_visible() and title.inner_text() == "Products"

    def sort_by(self, option_label: str) -> None:
        option_value = SORT_OPTIONS[option_label]
        self.page.locator(self.SORT_CONTAINER).select_option(value=option_value)
        self.page.locator(self.ITEM_PRICE).first.wait_for(state="visible")

    def get_product_prices(self) -> list[float]:
        prices = self.page.locator(self.ITEM_PRICE).all_inner_texts()
        return [parse_price(price) for price in prices]
