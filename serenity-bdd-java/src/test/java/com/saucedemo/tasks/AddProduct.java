package com.saucedemo.tasks;

import com.saucedemo.ui.InventoryPage;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.JavaScriptClick;
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers;
import net.serenitybdd.screenplay.waits.WaitUntil;

public class AddProduct implements Task {

    private final String productName;

    public AddProduct(String productName) {
        this.productName = productName;
    }

    public static AddProduct named(String productName) {
        return Tasks.instrumented(AddProduct.class, productName);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                WaitUntil.the(InventoryPage.ADD_TO_CART_BUTTON.of(productName),
                        WebElementStateMatchers.isVisible()).forNoMoreThan(10).seconds(),
                JavaScriptClick.on(InventoryPage.ADD_TO_CART_BUTTON.of(productName))
        );
    }
}
