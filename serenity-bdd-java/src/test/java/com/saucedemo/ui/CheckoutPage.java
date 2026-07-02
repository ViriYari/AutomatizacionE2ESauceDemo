package com.saucedemo.ui;

import net.serenitybdd.screenplay.targets.Target;

public class CheckoutPage {

    public static final Target FIRST_NAME_FIELD = Target.the("campo nombre")
            .locatedBy("[data-test='firstName']");

    public static final Target LAST_NAME_FIELD = Target.the("campo apellido")
            .locatedBy("[data-test='lastName']");

    public static final Target ZIP_CODE_FIELD = Target.the("campo código postal")
            .locatedBy("[data-test='postalCode']");

    public static final Target CONTINUE_BUTTON = Target.the("botón continuar")
            .locatedBy("[data-test='continue']");

    public static final Target FINISH_BUTTON = Target.the("botón finalizar compra")
            .locatedBy("[data-test='finish']");

    public static final Target CONFIRMATION_MESSAGE = Target.the("mensaje de confirmación de compra")
            .locatedBy("[data-test='complete-header']");

    private CheckoutPage() {
    }
}
