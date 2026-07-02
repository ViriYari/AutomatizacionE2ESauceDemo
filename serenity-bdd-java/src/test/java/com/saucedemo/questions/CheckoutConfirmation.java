package com.saucedemo.questions;

import com.saucedemo.ui.CheckoutPage;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;

public class CheckoutConfirmation {

    private CheckoutConfirmation() {
    }

    public static Question<String> message() {
        return Text.of(CheckoutPage.CONFIRMATION_MESSAGE);
    }
}
