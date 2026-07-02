package com.saucedemo.stepdefinitions;

import com.saucedemo.questions.CartBadge;
import com.saucedemo.questions.ProductVisibility;
import com.saucedemo.tasks.AddProduct;
import com.saucedemo.tasks.OpenTheCart;
import com.saucedemo.ui.CartPage;
import com.saucedemo.ui.InventoryPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers;
import net.serenitybdd.screenplay.waits.WaitUntil;

public class AddProductStepDefinitions {

    @Given("que el usuario se encuentra en el catálogo de productos de Sauce Demo")
    public void queElUsuarioSeEncuentraEnElCatalogoDeProductosDeSauceDemo() {
        OnStage.theActorInTheSpotlight().attemptsTo(
                WaitUntil.the(InventoryPage.PRODUCT_NAME.of("Sauce Labs Backpack"),
                        WebElementStateMatchers.isVisible()).forNoMoreThan(10).seconds(),
                Ensure.that(ProductVisibility.inCatalog("Sauce Labs Backpack")).isTrue()
        );
    }

    @When("el usuario selecciona el producto {string} para añadirlo al carrito")
    public void elUsuarioSeleccionaElProductoParaAnadirloAlCarrito(String productName) {
        OnStage.theActorInTheSpotlight().attemptsTo(
                AddProduct.named(productName)
        );
    }

    @And("el ícono del carrito de compras debería mostrar {string} artículo agregado")
    public void elIconoDelCarritoDeComprasDeberiaMostrarArticuloAgregado(String cantidad) {
        OnStage.theActorInTheSpotlight().attemptsTo(
                WaitUntil.the(CartPage.SHOPPING_CART_BADGE,
                        WebElementStateMatchers.containsText(cantidad)).forNoMoreThan(10).seconds(),
                Ensure.that(CartBadge.count()).isEqualTo(cantidad)
        );
    }

    @Then("el producto {string} debería estar presente en el carrito de compras")
    public void elProductoDeberiaEstarPresenteEnElCarrito(String nombreProducto) {
        OnStage.theActorInTheSpotlight().attemptsTo(
                OpenTheCart.page(),
                Ensure.that(ProductVisibility.inCart(nombreProducto)).isTrue()
        );
    }
}
