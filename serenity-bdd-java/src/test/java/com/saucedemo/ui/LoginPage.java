package com.saucedemo.ui;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public class LoginPage {

    public static final Target USERNAME_FIELD = Target.the("campo de usuario")
            .locatedBy("[data-test='username']");

    public static final Target PASSWORD_FIELD = Target.the("campo de contraseña")
            .locatedBy("[data-test='password']");

    public static final Target LOGIN_BUTTON = Target.the("botón de iniciar sesión")
            .locatedBy("[data-test='login-button']");

    private LoginPage() {
    }
}
