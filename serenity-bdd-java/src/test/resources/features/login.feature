@Login
Feature: Autenticación de Usuario

  Scenario: Inicio de sesión exitoso con credenciales válidas
    Given que el usuario está en la página de inicio de sesión de Sauce Demo
    When el usuario inicia sesión con credenciales válidas
    Then el usuario debería ser redirigido a la página del catálogo de productos
