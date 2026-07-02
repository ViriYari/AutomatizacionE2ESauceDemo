package com.saucedemo.tasks;

import com.saucedemo.ui.CartPage;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.JavaScriptClick;
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers;
import net.serenitybdd.screenplay.waits.WaitUntil;

public class OpenTheCart {

    private OpenTheCart() {
    }

    public static Task page() {
        return Task.where("{0} abre el carrito de compras",
                WaitUntil.the(CartPage.CART_ICON, WebElementStateMatchers.isVisible())
                        .forNoMoreThan(10).seconds(),
                JavaScriptClick.on(CartPage.CART_ICON)
        );
    }
}
