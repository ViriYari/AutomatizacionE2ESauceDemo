package com.saucedemo.tasks;

import com.saucedemo.ui.CartPage;
import com.saucedemo.ui.CheckoutPage;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.JavaScriptClick;
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers;
import net.serenitybdd.screenplay.waits.WaitUntil;

public class StartCheckout {

    private StartCheckout() {
    }

    public static Task now() {
        return Task.where("{0} inicia el proceso de checkout",
                WaitUntil.the(CartPage.CHECKOUT_BUTTON, WebElementStateMatchers.isClickable())
                        .forNoMoreThan(10).seconds(),
                JavaScriptClick.on(CartPage.CHECKOUT_BUTTON),
                WaitUntil.the(CheckoutPage.FIRST_NAME_FIELD, WebElementStateMatchers.isVisible())
                        .forNoMoreThan(10).seconds()
        );
    }
}
