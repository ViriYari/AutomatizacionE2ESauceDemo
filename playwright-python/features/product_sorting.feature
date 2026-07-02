@Ordenamiento @Playwright
Feature: Ordenamiento de productos en el catálogo

  Scenario: Ordenar productos por precio de menor a mayor
    Given que un usuario autenticado visualiza el catálogo de productos
    When ordena los productos por "Price (low to high)"
    Then los productos deberían mostrarse ordenados por precio ascendente
