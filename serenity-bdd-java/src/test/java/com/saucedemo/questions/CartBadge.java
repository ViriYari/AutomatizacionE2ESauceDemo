package com.saucedemo.questions;

import com.saucedemo.ui.CartPage;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;

public class CartBadge {

    private CartBadge() {
    }

    public static Question<String> count() {
        return Text.of(CartPage.SHOPPING_CART_BADGE);
    }
}
