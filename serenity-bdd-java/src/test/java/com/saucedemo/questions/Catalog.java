package com.saucedemo.questions;

import com.saucedemo.ui.CheckoutPage;
import com.saucedemo.ui.InventoryPage;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.questions.Visibility;

public class Catalog {

    private Catalog() {
    }

    public static Question<Boolean> isDisplayed() {
        return Visibility.of(InventoryPage.CATALOG_TITLE);
    }

    public static Question<String> title() {
        return Text.of(InventoryPage.CATALOG_TITLE);
    }
}
