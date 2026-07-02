# Automatización E2E — Sauce Demo

Proyecto de automatización End-to-End sobre [Sauce Demo](https://www.saucedemo.com/) con dos frameworks:

| Framework | Lenguaje | Escenarios | Enfoque |
|-----------|----------|------------|---------|
| **Serenity BDD** | Java | 3 | Login, carrito y checkout |
| **Playwright** | Python | 2 | Usuario bloqueado y ordenamiento |

**Total: 5 escenarios BDD** (Given / When / Then)

---

## Estructura del repositorio

```
automation-e2e-saucedemo/
├── Jenkinsfile                 # Pipeline CI/CD (Serenity + Playwright)
├── README.md
├── evidencias/                 # Documento y referencias de entrega
├── serenity-bdd-java/          # Proyecto Serenity Screenplay + Cucumber
└── playwright-python/          # Proyecto Playwright + pytest-bdd
```

---

## Requisitos del entorno

### Serenity BDD (Java)

| Requisito | Versión mínima |
|-----------|----------------|
| Java JDK | 21 |
| Apache Maven | 3.8+ |
| Google Chrome | Última estable |
| ChromeDriver | Automático (`autodriver=true` en Serenity) |

### Playwright (Python)

| Requisito | Versión mínima |
|-----------|----------------|
| Python | 3.10+ |
| pip | Actualizado |
| Navegador Chromium | Instalado vía Playwright CLI |

### Jenkins (opcional — CI/CD)

| Requisito |
|-----------|
| Jenkins con plugins: Pipeline, HTML Publisher, JUnit, Workspace Cleanup |
| JDK 21 y Maven 3.9 configurados en *Global Tool Configuration* |
| Python 3 disponible en el agente |
| Chrome/Chromium en el agente (headless) |

---

## Instalación

### 1. Clonar el repositorio

```bash
git clone <url-del-repositorio>
cd automation-e2e-saucedemo
```

### 2. Serenity BDD

No requiere instalación manual de dependencias; Maven las descarga automáticamente.

```bash
cd serenity-bdd-java
mvn clean test-compile
```

### 3. Playwright (Python)

```bash
cd playwright-python
python -m venv .venv

# Windows (PowerShell)
.\.venv\Scripts\Activate.ps1

# Linux / macOS
source .venv/bin/activate

pip install -r requirements.txt
playwright install chromium
```

---

## Ejecución de pruebas

### Serenity BDD — todos los escenarios

```bash
cd serenity-bdd-java
mvn clean test
```

**Reporte Serenity:** `serenity-bdd-java/target/site/serenity/index.html`

#### Ejecutar con entorno CI (headless)

```bash
mvn clean test -Denvironment=ci
```

#### Filtrar por tag Cucumber

```bash
mvn clean test -Dcucumber.filter.tags=@Login
mvn clean test -Dcucumber.filter.tags="@AgregarProducto or @CheckOut"
```

---

### Playwright — todos los escenarios

```bash
cd playwright-python

# Activar entorno virtual (ver instalación)
pytest
```

**Reporte HTML:** `playwright-python/reports/report.html`  
**JUnit XML (Jenkins/CI):** `playwright-python/reports/junit.xml`  
**Allure Report:** `playwright-python/reports/allure-report/index.html` (requiere Allure CLI o Node.js)  
**Allure raw results:** `playwright-python/reports/allure-results/`  
**Traces (fallos):** `playwright-python/test-results/` → abrir con `playwright show-trace <trace.zip>`  
**Screenshots en fallo:** `playwright-python/reports/screenshots/`

---

### Jenkins (CI/CD del reto)

1. Crear un job tipo **Pipeline** en Jenkins.
2. **Pipeline script from SCM** → URL del repositorio Git.
3. **Script Path:** `Jenkinsfile` (J mayúscula, en la raíz del repo).
4. **Branch:** `main`
5. Ejecutar **Build Now**.

**Plugins requeridos:** Pipeline, Git, HTML Publisher, JUnit, Workspace Cleanup

**En el agente Jenkins:** Java 11+, Maven 3.8+, Python 3.10+, Chrome/Chromium

> El bloque `tools { jdk / maven }` en el Jenkinsfile está comentado por defecto.
> Descoméntalo si tienes `JDK-11` y `Maven-3.9` configurados en *Global Tool Configuration*.

**Stages obligatorios del reto:**

| Stage | Acción |
|-------|--------|
| Stage 1: Ejecución Serenity | `mvn clean test` |
| Stage 2: Ejecución Playwright | `pytest` en entorno virtual |

**Reportes publicados en Jenkins:**

- Serenity BDD Report → `index.html`
- Playwright Report → `report.html`

---

## Escenarios automatizados

### Serenity BDD (3)

| # | Feature | Descripción |
|---|---------|-------------|
| 1 | `login.feature` | Login exitoso con credenciales válidas |
| 2 | `AddProduct.feature` | Agregar producto al carrito y validar badge |
| 3 | `CheckOut.feature` | Flujo completo de checkout hasta confirmación |

### Playwright (2)

| # | Feature | Descripción |
|---|---------|-------------|
| 4 | `locked_user.feature` | Login con usuario bloqueado (`locked_out_user`) |
| 5 | `product_sorting.feature` | Ordenar productos por precio ascendente |

---

## Credenciales de prueba (Sauce Demo)

| Usuario | Contraseña | Uso |
|---------|------------|-----|
| `standard_user` | `secret_sauce` | Flujos exitosos |
| `locked_out_user` | `secret_sauce` | Usuario bloqueado (Playwright) |

---

## Arquitectura

### Serenity — Screenplay Pattern

```
com.saucedemo/
├── config/          TestData (URL, credenciales, actor)
├── ui/              Page Objects (Targets)
├── tasks/           Acciones de negocio
├── questions/       Validaciones
├── interactions/    Interacciones custom (React inputs)
└── stepdefinitions/ Glue Cucumber + Hooks
```

### Playwright — Page Object Model + pytest-bdd

```
playwright-python/
├── features/        Escenarios Gherkin
├── steps/           Step definitions
├── pages/           Page Objects
├── config/          Configuración centralizada
└── utils/           Helpers (parseo de precios)
```

---

## Evidencias de entrega

Consulta el documento completo en:

**[`evidencias/EVIDENCIAS.md`](evidencias/EVIDENCIAS.md)**

Incluye escenarios BDD, rutas de reportes, screenshots y guía para evidencias de Jenkins.

---

## Solución de problemas

| Problema | Solución |
|----------|----------|
| Chrome no abre en CI | Usar `-Denvironment=ci` (Serenity) o headless en `conftest.py` (Playwright) |
| Locators no encontrados | Sauce Demo usa `data-test` en kebab-case (`shopping-cart-link`) |
| Playwright: browser not installed | Ejecutar `playwright install chromium` |
| Maven: tests no corren | Verificar que el runner esté en `**/runners/*.java` |

---

## Autor / Reto

Proyecto desarrollado como estrategia de automatización E2E — Reto 2.  
Aplicación bajo prueba: https://www.saucedemo.com/
