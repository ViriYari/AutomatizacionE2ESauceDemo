package com.saucedemo.tasks;

import com.saucedemo.interactions.SetReactInputValue;
import com.saucedemo.ui.CheckoutPage;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.JavaScriptClick;
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers;
import net.serenitybdd.screenplay.waits.WaitUntil;

public class CheckOut implements Task {

    private final String firstName;
    private final String lastName;
    private final String zipCode;

    public CheckOut(String firstName, String lastName, String zipCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.zipCode = zipCode;
    }

    public static CheckOut withInformation(String firstName, String lastName, String zipCode) {
        return Tasks.instrumented(CheckOut.class, firstName, lastName, zipCode);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                SetReactInputValue.to(CheckoutPage.FIRST_NAME_FIELD, firstName),
                SetReactInputValue.to(CheckoutPage.LAST_NAME_FIELD, lastName),
                SetReactInputValue.to(CheckoutPage.ZIP_CODE_FIELD, zipCode),
                JavaScriptClick.on(CheckoutPage.CONTINUE_BUTTON),
                WaitUntil.the(CheckoutPage.FINISH_BUTTON, WebElementStateMatchers.isVisible())
                        .forNoMoreThan(10).seconds(),
                JavaScriptClick.on(CheckoutPage.FINISH_BUTTON)
        );
    }
}
