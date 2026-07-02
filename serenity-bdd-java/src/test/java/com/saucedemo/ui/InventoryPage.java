package com.saucedemo.ui;

import net.serenitybdd.screenplay.targets.Target;

public class InventoryPage {

    public static final Target CATALOG_TITLE = Target.the("título del catálogo de productos")
            .locatedBy("[data-test='title']");

    public static final Target ADD_TO_CART_BUTTON = Target.the("botón agregar al carrito de {0}")
            .locatedBy("//div[contains(@class,'inventory_item_name') and normalize-space()='{0}']"
                    + "/ancestor::div[contains(@class,'inventory_item')]"
                    + "//button[contains(@data-test,'add-to-cart')]");

    public static final Target PRODUCT_NAME = Target.the("nombre del producto {0}")
            .locatedBy("//*[normalize-space(text())='{0}']");

    private InventoryPage() {
    }
}
