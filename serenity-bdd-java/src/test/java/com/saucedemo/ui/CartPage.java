package com.saucedemo.ui;

import net.serenitybdd.screenplay.targets.Target;

public class CartPage {

    public static final Target CART_ICON = Target.the("ícono del carrito de compras")
            .locatedBy("[data-test='shopping-cart-link']");

    public static final Target SHOPPING_CART_BADGE = Target.the("número indicador del carrito")
            .locatedBy("[data-test='shopping-cart-badge']");

    public static final Target CHECKOUT_BUTTON = Target.the("botón de checkout")
            .locatedBy("[data-test='checkout']");

    public static final Target PRODUCT_NAME = Target.the("nombre del producto {0} en el carrito")
            .locatedBy("//div[contains(@class,'cart_item')]//*[normalize-space(text())='{0}']");

    private CartPage() {
    }
}
