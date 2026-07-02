@AgregarProducto
Feature: Agregar productos al carrito de compras

  @LoginRequerido
  Scenario: Agregar un producto exitosamente desde el catálogo
    Given que el usuario se encuentra en el catálogo de productos de Sauce Demo
    When el usuario selecciona el producto "Sauce Labs Backpack" para añadirlo al carrito
    And el ícono del carrito de compras debería mostrar "1" artículo agregado
    Then el producto "Sauce Labs Backpack" debería estar presente en el carrito de compras
