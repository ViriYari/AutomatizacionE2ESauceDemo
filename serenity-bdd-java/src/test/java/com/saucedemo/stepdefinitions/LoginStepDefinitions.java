package com.saucedemo.stepdefinitions;

import com.saucedemo.config.TestData;
import com.saucedemo.questions.Catalog;
import com.saucedemo.tasks.Login;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.actions.Open;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.ensure.Ensure;

public class LoginStepDefinitions {

    @Given("que el usuario está en la página de inicio de sesión de Sauce Demo")
    public void queElUsuarioEstaEnLaPaginaDeInicioDeSesionDeSauceDemo() {
        OnStage.theActorCalled(TestData.ACTOR_NAME).attemptsTo(
                Open.url(TestData.BASE_URL)
        );
    }

    @When("el usuario inicia sesión con credenciales válidas")
    public void elUsuarioIniciaSesionConCredencialesValidas() {
        OnStage.theActorInTheSpotlight().attemptsTo(
                Login.withCredentials(TestData.STANDARD_USER, TestData.STANDARD_PASSWORD)
        );
    }

    @Then("el usuario debería ser redirigido a la página del catálogo de productos")
    public void elUsuarioDeberiaSerRedirigidoALaPaginaDelCatalogoDeProductos() {
        OnStage.theActorInTheSpotlight().attemptsTo(
                Ensure.that(Catalog.isDisplayed()).isTrue(),
                Ensure.that(Catalog.title()).isEqualTo("Products")
        );
    }
}
