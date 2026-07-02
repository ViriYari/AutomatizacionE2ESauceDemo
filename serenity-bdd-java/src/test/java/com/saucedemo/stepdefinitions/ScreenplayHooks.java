package com.saucedemo.stepdefinitions;

import com.saucedemo.config.TestData;
import com.saucedemo.ui.InventoryPage;
import com.saucedemo.tasks.Login;
import io.cucumber.java.Before;
import net.serenitybdd.screenplay.actions.Open;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers;
import net.serenitybdd.screenplay.waits.WaitUntil;

public class ScreenplayHooks {

    @Before(order = 0)
    public void setTheStage() {
        OnStage.setTheStage(new OnlineCast());
    }

    @Before(order = 1, value = "@LoginRequerido")
    public void loginAsStandardUser() {
        OnStage.theActorCalled(TestData.ACTOR_NAME);
        OnStage.theActorInTheSpotlight().attemptsTo(
                Open.url(TestData.BASE_URL),
                Login.withCredentials(TestData.STANDARD_USER, TestData.STANDARD_PASSWORD),
                WaitUntil.the(InventoryPage.CATALOG_TITLE, WebElementStateMatchers.isVisible())
                        .forNoMoreThan(10).seconds()
        );
    }
}
