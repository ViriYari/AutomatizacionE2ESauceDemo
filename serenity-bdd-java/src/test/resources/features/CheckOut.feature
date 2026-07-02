@CheckOut
Feature: Proceso de checkout en Sauce Demo

  @LoginRequerido
  Scenario: Completar el proceso de checkout exitosamente
    Given que el usuario tiene un producto agregado al carrito de compras
    When el usuario procede a realizar el checkout
    And completa la información requerida para finalizar la compra
    Then debería ver una confirmación de que la compra se ha realizado exitosamente
