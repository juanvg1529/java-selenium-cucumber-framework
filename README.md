# Java Selenium Cucumber Framework

[![UI & API Tests](https://github.com/juanvg1529/java-selenium-cucumber-framework/actions/workflows/tests.yml/badge.svg)](https://github.com/juanvg1529/java-selenium-cucumber-framework/actions/workflows/tests.yml)
[![Report](https://img.shields.io/badge/report-Cucumber-23D96C)](https://juanvg1529.github.io/java-selenium-cucumber-framework/)
![Java 21](https://img.shields.io/badge/Java-21-orange)
![Selenium 4](https://img.shields.io/badge/Selenium-4.51-43B02A)

BDD test automation in **Java** with **Selenium WebDriver** for the UI and **RestAssured** for the API, written in Cucumber:

- UI: [Swag Labs](https://www.saucedemo.com) e-commerce demo.
- API: the public [Swagger Petstore](https://petstore.swagger.io).

**→ [Latest Cucumber report](https://juanvg1529.github.io/java-selenium-cucumber-framework/)**

## Coverage

18 scenarios:

| Feature | Scenarios |
|---|---|
| Login | Standard user logs in and out; locked-out user, wrong password, missing username and missing password are rejected with the exact error |
| Purchase | Buy each of 3 products; buy several products after removing one; buy the 2nd product after sorting Z→A; checkout without first name is blocked. Each purchase checks that the overview lists exactly the cart, payment and shipping info, and that **total = item total + tax** (with `BigDecimal`, no floating-point rounding) |
| Sorting | Name A→Z / Z→A and price low→high / high→low, verifying the order of the whole catalogue |
| Petstore API | Full lifecycle (create → read → update → delete → 404), list by status, unknown pet → 404 with message |

Tags: `@ui`, `@api`, `@smoke`, `@regression`, `@negative`.

## Stack

Java 21 · Selenium 4 (Selenium Manager, no driver binaries) · Cucumber 8 on the JUnit Platform · PicoContainer (dependency injection) · RestAssured · Jackson · AssertJ · Maven · GitHub Actions

## Architecture

```
src/test/java/com/jdvg/automation/
├── config/TestConfig          config.properties, overridable with -Dkey=value or env vars
├── driver/                    Browser enum + DriverFactory (Chrome, Firefox, Edge; headless option)
├── context/TestContext        per-scenario state injected by PicoContainer (driver, cart, pet, response)
├── hooks/Hooks                screenshot on failure, browser shutdown, API cleanup
├── pages/                     page objects (Login, Inventory, Cart, Checkout x3) on a BasePage with explicit waits
├── api/                       PetStoreClient (RestAssured), Pet record, PetBuilder
└── steps/                     Login, Shopping and PetStore step definitions

src/test/resources/
├── features/ui, features/api  Gherkin
├── config.properties
└── junit-platform.properties  Cucumber options and parallel execution
```

## Design decisions

- **No static state.** The driver and scenario data live in a `TestContext` that PicoContainer creates for each scenario and injects into steps and hooks. That's what makes **parallel execution** (4 threads) safe.
- **Stable locators.** Every locator uses SauceDemo's `data-test` attributes. There are no absolute XPaths, so layout changes don't break the tests.
- **Explicit waits only.** `BasePage` wraps `WebDriverWait` (visible, clickable, retry on stale elements). There are no implicit waits and no `Thread.sleep` in page objects. React re-renders, such as removing an item from the cart, are handled by waiting for the new state.
- **Selenium Manager instead of committed drivers.** Selenium 4.6+ downloads the right driver for the installed browser. The browser is chosen with `-Dbrowser=chrome|firefox|edge`.
- **Real checks, not just navigation.**
  - Purchases compare the overview with what the scenario put in the cart.
  - The money maths is recalculated: total = item total + tax.
  - Sorting checks the whole list, not a single item.
- **Isolated API data.** Each pet gets a random id. The scenario deletes it, and a hook deletes it again if the scenario failed half-way. Reads after writes are polled briefly, because the public Petstore runs on several nodes that sync with a delay.
- **Failures are failures.** Surefire no longer ignores test failures, and every failed UI scenario attaches a screenshot to the report.

## Running locally

Requires JDK 21, Maven, and Chrome (or Firefox/Edge).

```bash
mvn test                                              # all scenarios, headless Chrome
mvn test -Dheadless=false                             # watch the browser
mvn test -Dbrowser=firefox
mvn test -Dcucumber.filter.tags="@smoke"
mvn test -Dcucumber.filter.tags="@api"
```

The report is written to `target/cucumber-report/index.html`.

## History

This project began in 2024 as my Selenium + Cucumber + RestAssured practice suite (see the commit history). In 2026 I modernised it:

| Before | After |
|---|---|
| Java 15 | Java 21 |
| Driver `.exe` files committed to the repo | Selenium Manager |
| Static driver and context | Dependency injection and parallel execution |
| Absolute XPaths | `data-test` locators |
| Fixed API ids | Unique ids with cleanup |
| `testFailureIgnore` hid failures | Failures fail the build |
| Runner pointed at a wrongly cased folder (UI tests never ran on Linux) | Fixed |
| No CI | CI with a published report |

---

Built by **Juan Diego Vera Godoy**, QA Automation Engineer / SDET · [LinkedIn](https://www.linkedin.com/in/juan-diego-vera-godoy-3949a6183) · More: [FHIR R4 API Automation](https://github.com/juanvg1529/fhir-api-automation) · [Playwright Hybrid Framework](https://github.com/juanvg1529/MonsterAutomation) · [.NET API Automation Framework](https://github.com/juanvg1529/dotnet-api-automation-framework)
