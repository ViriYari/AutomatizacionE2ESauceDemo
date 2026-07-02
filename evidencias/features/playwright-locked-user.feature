@Login @Playwright
Feature: Autenticación con usuario bloqueado

  Scenario: El sistema impide el acceso a un usuario bloqueado
    Given que el usuario está en la página de inicio de sesión de Sauce Demo
    When intenta iniciar sesión con el usuario bloqueado "locked_out_user"
    Then debería ver un mensaje de error indicando que el usuario está bloqueado
    And no debería acceder al catálogo de productos
