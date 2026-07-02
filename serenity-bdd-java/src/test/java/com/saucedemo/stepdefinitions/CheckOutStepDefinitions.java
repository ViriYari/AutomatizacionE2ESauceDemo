package com.saucedemo.stepdefinitions;

import com.saucedemo.questions.CheckoutConfirmation;
import com.saucedemo.tasks.AddProduct;
import com.saucedemo.tasks.CheckOut;
import com.saucedemo.tasks.OpenTheCart;
import com.saucedemo.tasks.StartCheckout;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.ensure.Ensure;

public class CheckOutStepDefinitions {

    @Given("que el usuario tiene un producto agregado al carrito de compras")
    public void queElUsuarioTieneUnProductoAgregadoAlCarritoDeCompras() {
        OnStage.theActorInTheSpotlight().attemptsTo(
                AddProduct.named("Sauce Labs Backpack"),
                OpenTheCart.page()
        );
    }

    @When("el usuario procede a realizar el checkout")
    public void elUsuarioProcedeARealizarElCheckout() {
        OnStage.theActorInTheSpotlight().attemptsTo(
                StartCheckout.now()
        );
    }

    @When("completa la información requerida para finalizar la compra")
    public void elUsuarioCompletaLaInformacionRequeridaParaFinalizarLaCompra() {
        OnStage.theActorInTheSpotlight().attemptsTo(
                CheckOut.withInformation("John", "Doe", "12345")
        );
    }

    @Then("debería ver una confirmación de que la compra se ha realizado exitosamente")
    public void deberiaVerUnaConfirmacionDeQueLaCompraSeHaRealizadoExitosamente() {
        OnStage.theActorInTheSpotlight().attemptsTo(
                Ensure.that(CheckoutConfirmation.message()).isEqualTo("Thank you for your order!")
        );
    }
}
