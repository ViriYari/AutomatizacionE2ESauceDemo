package com.saucedemo.questions;

import com.saucedemo.ui.CartPage;
import com.saucedemo.ui.InventoryPage;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Visibility;

public class ProductVisibility {

    private ProductVisibility() {
    }

    public static Question<Boolean> inCatalog(String productName) {
        return Visibility.of(InventoryPage.PRODUCT_NAME.of(productName));
    }

    public static Question<Boolean> inCart(String productName) {
        return Visibility.of(CartPage.PRODUCT_NAME.of(productName));
    }
}
