package com.saucedemo.interactions;

import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.JavascriptExecutor;

public class SetReactInputValue implements Interaction {

    private final Target field;
    private final String value;

    public SetReactInputValue(Target field, String value) {
        this.field = field;
        this.value = value;
    }

    public static Interaction to(Target field, String value) {
        return new SetReactInputValue(field, value);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        WebElementFacade element = field.resolveFor(actor);
        element.waitUntilVisible();
        JavascriptExecutor js = (JavascriptExecutor) BrowseTheWeb.as(actor).getDriver();
        js.executeScript(
                "const element = arguments[0];"
                        + "const value = arguments[1];"
                        + "const setter = Object.getOwnPropertyDescriptor("
                        + "window.HTMLInputElement.prototype, 'value').set;"
                        + "setter.call(element, value);"
                        + "element.dispatchEvent(new Event('input', { bubbles: true }));"
                        + "element.dispatchEvent(new Event('change', { bubbles: true }));",
                element, value
        );
    }
}
