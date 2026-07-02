# Documento de Evidencias — Reto 2 E2E Sauce Demo

**Proyecto:** Automatización End-to-End sobre https://www.saucedemo.com/  
**Fecha de referencia:** Junio 2026  
**Frameworks:** Serenity BDD (Java) + Playwright (Python)

---

## 1. Resumen ejecutivo

> **PDF de entrega:** [`EVIDENCIAS-Reto2-E2E-SauceDemo.pdf`](EVIDENCIAS-Reto2-E2E-SauceDemo.pdf)  
> Regenerar: `python evidencias/generar_evidencias_pdf.py` (requiere pruebas ejecutadas y venv Playwright).

| Métrica | Valor |
|---------|-------|
| Total escenarios BDD | 5 |
| Serenity BDD | 3 escenarios |
| Playwright | 2 escenarios |
| Pipeline CI/CD | Jenkinsfile con Stage 1 (Serenity) + Stage 2 (Playwright) |
| Última ejecución Serenity documentada | 3/3 passed |
| Última ejecución Playwright documentada | 2/2 passed |

---

## 2. Escenarios BDD (Gherkin)

Copias de referencia en `evidencias/features/`.

### 2.1 Serenity BDD — Java (3 escenarios)

#### Escenario 1 — Login exitoso

**Archivo:** `serenity-bdd-java/src/test/resources/features/login.feature`

```gherkin
@Login
Feature: Autenticación de Usuario

  Scenario: Inicio de sesión exitoso con credenciales válidas
    Given que el usuario está en la página de inicio de sesión de Sauce Demo
    When el usuario inicia sesión con credenciales válidas
    Then el usuario debería ser redirigido a la página del catálogo de productos
```

**Cobertura funcional:** Autenticación exitosa → acceso al catálogo.

---

#### Escenario 2 — Agregar producto al carrito

**Archivo:** `serenity-bdd-java/src/test/resources/features/AddProduct.feature`

```gherkin
@AgregarProducto
Feature: Agregar productos al carrito de compras

  @LoginRequerido
  Scenario: Agregar un producto exitosamente desde el catálogo
    Given que el usuario se encuentra en el catálogo de productos de Sauce Demo
    When el usuario selecciona el producto "Sauce Labs Backpack" para añadirlo al carrito
    And el ícono del carrito de compras debería mostrar "1" artículo agregado
    Then el producto "Sauce Labs Backpack" debería estar presente en el carrito de compras
```

**Cobertura funcional:** Inventario → carrito de compras.

---

#### Escenario 3 — Checkout completo

**Archivo:** `serenity-bdd-java/src/test/resources/features/CheckOut.feature`

```gherkin
@CheckOut
Feature: Proceso de checkout en Sauce Demo

  @LoginRequerido
  Scenario: Completar el proceso de checkout exitosamente
    Given que el usuario tiene un producto agregado al carrito de compras
    When el usuario procede a realizar el checkout
    And completa la información requerida para finalizar la compra
    Then debería ver una confirmación de que la compra se ha realizado exitosamente
```

**Cobertura funcional:** Carrito → checkout → confirmación de compra.

---

### 2.2 Playwright — Python (2 escenarios)

#### Escenario 4 — Usuario bloqueado

**Archivo:** `playwright-python/features/locked_user.feature`

```gherkin
@Login @Playwright
Feature: Autenticación con usuario bloqueado

  Scenario: El sistema impide el acceso a un usuario bloqueado
    Given que el usuario está en la página de inicio de sesión de Sauce Demo
    When intenta iniciar sesión con el usuario bloqueado "locked_out_user"
    Then debería ver un mensaje de error indicando que el usuario está bloqueado
    And no debería acceder al catálogo de productos
```

**Cobertura funcional:** Autenticación negativa / usuario bloqueado.

---

#### Escenario 5 — Ordenamiento por precio

**Archivo:** `playwright-python/features/product_sorting.feature`

```gherkin
@Ordenamiento @Playwright
Feature: Ordenamiento de productos en el catálogo

  Scenario: Ordenar productos por precio de menor a mayor
    Given que un usuario autenticado visualiza el catálogo de productos
    When ordena los productos por "Price (low to high)"
    Then los productos deberían mostrarse ordenados por precio ascendente
```

**Cobertura funcional:** Catálogo → ordenamiento → validación de precios.

---

## 3. Distribución de funcionalidades por herramienta

| Funcionalidad | Serenity | Playwright |
|---------------|:--------:|:----------:|
| Login exitoso | ✅ | — |
| Agregar al carrito | ✅ | — |
| Checkout E2E | ✅ | — |
| Usuario bloqueado | — | ✅ |
| Ordenamiento de productos | — | ✅ |

---

## 4. Evidencias de ejecución local

### 4.1 Serenity BDD

| Evidencia | Ubicación |
|-----------|-----------|
| Reporte HTML principal | `serenity-bdd-java/target/site/serenity/index.html` |
| Resumen de ejecución | `evidencias/serenity/resumen-ejecucion.txt` |
| Screenshots | Generados en reporte Serenity (config: `AFTER_EACH_STEP`) |
| Surefire XML | `serenity-bdd-java/target/surefire-reports/` |

**Resultado documentado:**

```
Test Cases:  3
Passed:      3
Failed:      0
```

**Comando de reproducción:**

```bash
cd serenity-bdd-java
mvn clean test
```

---

### 4.2 Playwright

| Evidencia | Ubicación |
|-----------|-----------|
| Reporte HTML | `playwright-python/reports/report.html` |
| Screenshots en fallo | `playwright-python/reports/screenshots/` |
| Salida pytest | Consola / CI log |

**Resultado documentado:**

```
2 passed
- test_el_sistema_impide_el_acceso_a_un_usuario_bloqueado
- test_ordenar_productos_por_precio_de_menor_a_mayor
```

**Comando de reproducción:**

```bash
cd playwright-python
pytest
```

---

## 5. Evidencias de integración continua (Jenkins)

### 5.1 Configuración del pipeline

**Archivo:** `Jenkinsfile` (raíz del repositorio)

| Stage | Descripción |
|-------|-------------|
| Checkout | Clona el repositorio |
| Verificar entorno | Valida Java, Maven, Python |
| **Stage 1: Ejecución Serenity** | `mvn clean test` en `serenity-bdd-java/` |
| **Stage 2: Ejecución Playwright** | `pytest` en `playwright-python/` — **2/2 passed** |
| Pipeline completo | **SUCCESS** (Stage 1 + Stage 2) |
| Publicar reportes | HTML Publisher (Serenity + Playwright) |
| Archivar evidencias | Artefactos de ambos frameworks |
| post always | Limpieza del workspace |

### 5.2 Capturas requeridas para la entrega

Guardar en `evidencias/jenkins/`:

| # | Captura | Descripción |
|---|---------|-------------|
| 1 | `01-pipeline-overview.png` | Vista general del pipeline con stages |
| 2 | `02-stage1-serenity-success.png` | Stage 1 completado |
| 3 | `03-stage2-playwright-success.png` | Stage 2 completado |
| 4 | `04-serenity-report-link.png` | Enlace al reporte Serenity en Jenkins |
| 5 | `05-playwright-report-link.png` | Enlace al reporte Playwright en Jenkins |
| 6 | `06-archived-artifacts.png` | Artefactos archivados del build |

> **Estado:** Pipeline ejecutado con éxito (Stage 1 + Stage 2).  
> Opcional: agregar capturas PNG en `evidencias/jenkins/` y regenerar el PDF.

---

## 6. Buenas prácticas aplicadas

| Práctica | Serenity | Playwright |
|----------|:--------:|:----------:|
| Locators `data-test` | ✅ | ✅ |
| Sin XPath absolutos | ✅ | ✅ |
| Esperas explícitas | ✅ `WaitUntil` | ✅ `expect`, `wait_for` |
| Patrón Screenplay / POM | ✅ Screenplay | ✅ Page Objects |
| Código reutilizable | ✅ Tasks, Questions | ✅ Pages, utils |
| Reportes automáticos | ✅ Serenity reports | ✅ pytest-html |
| Screenshots en fallo | ✅ (reporte Serenity) | ✅ `conftest.py` hook |
| CI/CD Jenkins | ✅ | ✅ |

---

## 7. Checklist de entrega

| Entregable | Estado |
|------------|--------|
| Proyecto Serenity BDD | ✅ |
| Proyecto Playwright | ✅ |
| Jenkinsfile | ✅ |
| README.md | ✅ |
| Documento de evidencias | ✅ |
| Escenarios BDD documentados | ✅ |
| Evidencias ejecución local | ✅ (rutas documentadas) |
| Pipeline Jenkins (Stage 1 + 2) | ✅ SUCCESS |
| Capturas Jenkins PNG | ☐ Opcional — `evidencias/jenkins/` |
| PDF evidencias | ✅ `evidencias/EVIDENCIAS-Reto2-E2E-SauceDemo.pdf` |

---

## 8. Referencias rápidas

- Aplicación: https://www.saucedemo.com/
- README principal: [`../README.md`](../README.md)
- Serenity features: `serenity-bdd-java/src/test/resources/features/`
- Playwright features: `playwright-python/features/`
