# Test Plan for XYZ Bank Web Application — Automated Testing

---

## Document Control

| Version | Description | Date | Author |
|---|---|---|---|
| 1.0 | Initial Test Plan for XYZ Bank Automated Testing | 25 May 2026 | Teta Butera Nelly |

---

## 1. Introduction

This test plan defines what will be tested, how it will be tested, who will test it, and when testing
will happen for the XYZ Bank web application. It outlines the testing objectives, scope, resources,
design techniques, schedule, risks, and communication approach.

The XYZ Bank application is a digital banking platform serving two user groups — Bank Managers
and Customers — each with distinct functional requirements that must be validated. All testing
activities will be implemented as automated scripts using Selenium WebDriver with Java and TestNG,
following the Page Object Model (POM) design pattern. Test results will be reported using Allure
Report, and the pipeline will be containerised with Docker and executed via GitHub Actions CI.

---

## 2. Test Objectives

The following objectives define what this testing effort aims to achieve for the XYZ Bank
application.

- **Verification of Requirements:** Confirm that the application meets all functional requirements
  defined in the user stories and acceptance criteria through automated test execution.

- **Data Integrity:** Validate the integrity and consistency of customer data and account balances
  across deposit, withdrawal, and transaction operations.

- **Input Validation:** Verify that input fields enforce validation rules — alphabetic-only names,
  numeric-only postal codes, and positive numeric transaction amounts.

- **Access Control:** Ensure customers cannot access banking features without an active account
  and that deleted customers lose access immediately.

- **Defect Detection:** Detect and document defects exposed by automated test failures; each
  failing test serves as reproducible evidence of a defect.

- **Automation Coverage:** Validate all 20 planned test scenarios through automated scripts
  covering happy path, negative, and boundary value cases for both Bank Manager and Customer flows.

- **Continuous Integration:** Ensure the full test suite executes automatically on every code push
  via a GitHub Actions pipeline, with Allure reporting available after each run.

---

## 3. Scope of Testing

The scope defines what will and will not be tested. The table below lists each testing type, its
definition, whether it is included, and who is responsible for executing it.

| # | Type | Definition | In Scope | Responsibility |
|---|---|---|---|---|
| 1 | Functional Testing | Verifies each feature operates as specified across both Bank Manager and Customer flows | **Yes** | QA Engineer |
| 2 | Negative Testing | Uses invalid inputs and edge cases to confirm the system rejects incorrect data gracefully | **Yes** | QA Engineer |
| 3 | Boundary Value Analysis | Tests at the exact edges of allowed input ranges — zero, exact balance, amounts just above the limit | **Yes** | QA Engineer |
| 4 | Access Control Testing | Validates that system access restrictions are consistently enforced for unaccounted and deleted customers | **Yes** | QA Engineer |
| 5 | Regression Testing | Re-runs the full automated test suite after any change to confirm nothing broke | **Yes** | GitHub Actions (automated) |
| 6 | UI Testing | Tests navigation, form interactions, and feedback messages on Bank Manager and Customer dashboards via WebDriver | **Yes** | QA Engineer |
| 7 | Automated Testing | Selenium WebDriver + Java + TestNG scripts implementing the Page Object Model, run in Docker via GitHub Actions CI | **Yes** | QA Engineer |
| 8 | Performance Testing | Load and stress testing under heavy usage | **No** | Out of scope |
| 9 | Mobile / Cross-Browser | Testing on mobile devices and browsers beyond Chrome | **No** | Out of scope |
| 10 | Exploratory Testing | Unscripted manual exploration of the application | **No** | Out of scope |
| 11 | Penetration Testing | Deep security vulnerability assessment beyond input validation checks | **No** | Out of scope |

---

## 4. Testing Approach and Design Techniques

Test cases will be designed using the techniques described below to ensure thorough coverage of
valid scenarios, invalid inputs, and edge conditions. All test cases are implemented as TestNG
`@Test` methods organised by the Page Object Model.

| Technique | What It Means | Where Applied |
|---|---|---|
| Equivalence Partitioning | Split inputs into valid and invalid groups; test one representative value from each | Customer name and post code field validation |
| Boundary Value Analysis | Test at the exact edges of allowed input ranges where defects most often occur | Withdrawal amount equal to current balance; zero-value deposit and withdrawal attempts |
| State Transition Testing | Test transitions between system states — customer accounts move from inactive to active to deleted | Customer access before vs after account creation; access after customer deletion |
| Error Guessing | Use experience and intuition to predict where bugs are most likely to hide | Empty form submissions; special characters in name fields; missing dropdown selections |

### Framework Design Approach

- **Page Object Model (POM):** Each page of the application is represented by a dedicated Java
  class containing all locators and interaction methods for that page. Test classes contain only
  test logic and assertions — no WebDriver commands.

- **BaseTest class:** Handles WebDriver initialisation (`@BeforeMethod`) and teardown
  (`@AfterMethod`), and captures a screenshot on test failure for attachment to the Allure report.

- **Data-Driven Testing:** Test data (valid users, invalid inputs, boundary values) is stored in
  CSV files and fed into TestNG tests via `@DataProvider`, allowing a single test method to cover
  multiple input scenarios.

- **Allure Reporting:** Key POM methods are annotated with `@Step` to produce a structured,
  readable step-by-step trace in the Allure report. Screenshots of failures are attached
  automatically.

- **Execution Strategy:** All tests run sequentially (not in parallel) via TestNG. Sequential
  execution is preferred to avoid rate-limiting (HTTP 429) errors from the public demo app, which
  can occur when multiple browser sessions hit the server simultaneously.

---

## 5. Testing Resources and Tools

### Personnel

- **QA Engineer:** Designs and implements the test framework, writes all Page Objects and test
  classes, configures CI/CD, and generates Allure reports. Responsible for all test artifacts.
- **Development Team:** Supports defect investigation; provides technical clarification on
  functional behaviour when required.
- **Product Owner:** Reviews and clarifies requirements; validates that test coverage aligns with
  business expectations.
- **Reviewer:** Reviews the test plan and framework structure; evaluates execution results and
  provides final assessment.

### Tools

| Tool | Purpose |
|---|---|
| Java 17 | Primary programming language for all test code |
| Apache Maven | Build tool; manages dependencies (Selenium, TestNG, Allure) via `pom.xml` |
| Selenium WebDriver 4 | Browser automation library for driving Chrome |
| TestNG | Test runner — provides `@Test`, `@BeforeMethod`, `@AfterMethod`, `@DataProvider` annotations |
| WebDriverManager | Automatically resolves and pins the correct ChromeDriver version at runtime |
| Allure Report (allure-testng) | Generates interactive HTML test reports with steps, attachments, and trends; report is automatically published to GitHub Pages after each CI run for persistent, shareable access |
| Docker | Containerises the test environment for consistent, reproducible execution |
| GitHub Actions | CI/CD pipeline — runs the full test suite on every push and publishes the Allure report |
| IntelliJ IDEA | IDE for writing and debugging test code |
| Google Chrome (latest) | Target browser for all test execution |

---

## 6. Test Entry and Exit Criteria

### 6.1 Entry Criteria

Test execution will begin once the following conditions are met:

- User stories and acceptance criteria are documented and approved
- Maven project is configured with all required dependencies (`pom.xml` compiles cleanly)
- Page Object classes have been created for all pages under test
- `BaseTest` class is implemented with WebDriver setup and teardown
- Test data CSV files are prepared covering happy path, negative, and boundary scenarios
- Application is accessible at the test URL
- `testng.xml` suite file is configured and all tests run locally without setup errors
- Docker image builds successfully and runs the test suite in a container

### 6.2 Exit Criteria

Testing will be considered complete once the following are achieved:

- All 20 planned test cases have been executed with a recorded status (Pass / Fail)
- GitHub Actions CI pipeline runs the full suite successfully on push to main
- Allure report has been generated and shows step-level results with screenshots for all failures
- All identified defects have been documented with full reproduction steps and evidence
- Framework folder structure follows POM conventions and is committed to the repository
- Pull Request approved by Reviewer (Francis Asante Nsiah) in GitHub

---

## 7. Defect Management

Defects are exposed as failing automated tests. Each test failure constitutes a documented,
reproducible defect record: the Allure report captures the failing step, the assertion error message,
and a screenshot of the browser state at the point of failure.

Where a defect requires formal tracking, it will be logged as a GitHub Issue linked to the failing
test class and method, with the Allure screenshot attached as evidence.

| Severity | Priority | Definition | Example |
|---|---|---|---|
| **Critical** | High | Core feature broken or application crashes — blocks all further testing | Customer login fails entirely; deposit or withdrawal feature unresponsive |
| **High** | High | Major feature broken; workaround exists but significantly impacts users | Validation rule not enforced (numeric name accepted); withdrawal allows amount exceeding balance |
| **Medium** | Medium | Feature partially works; non-critical functionality affected | Search filter not working correctly; transaction timestamps inaccurate |
| **Low** | Low | Minor cosmetic or UI issue; does not affect functionality | Button label spacing inconsistent; minor alignment issues |

---

## 8. Testing Schedule

Testing will be conducted within a single sprint following the phases below. Timelines are expressed
relative to the sprint to allow flexibility in start dates.

| Phase | Timeline | Coverage | Remarks |
|---|---|---|---|
| Requirement Review & Test Design | Beginning of Sprint | User stories, acceptance criteria, 20 test scenarios | Review both user stories; map each AC to a TestNG test method; design test data CSV |
| Framework Setup | Beginning of Sprint | Maven project, POM folder structure, BaseTest, WebDriverManager | Project scaffolding, `pom.xml`, Page Object skeleton classes |
| Test Implementation | During Sprint | All 20 test cases across Bank Manager and Customer flows | Implement Page Objects and test classes; add `@Step` and `@DataProvider` annotations |
| CI/CD & Containerisation | During Sprint | GitHub Actions workflow, Dockerfile | Configure Docker image and GitHub Actions to run suite on push |
| Test Execution & Defect Logging | During Sprint | All in-scope test cases | Run full suite; document all failures; attach Allure evidence |
| Regression & Test Closure | End of Sprint | Full suite after any fixes; Allure report finalised | Re-run suite to confirm fixes; generate and export final Allure report |

---

## 9. Risks and Mitigations

| Risk | Impact | Why It Matters | Mitigation |
|---|---|---|---|
| Demo app data resets between sessions | **High** | The application is hosted on a public demo and data may not persist | Each test method creates its own prerequisite data (e.g. adds a customer before testing account creation). Tests are designed to be self-contained and order-independent |
| Test environment unavailability | **High** | Demo app is hosted by a third party and could go down without notice | Check availability before each session. Document downtime. GitHub Actions pipeline includes a pre-run URL health check step |
| WebDriver / ChromeDriver version mismatch | **High** | Chrome auto-updates can break a pinned ChromeDriver version, causing all tests to fail | Use WebDriverManager to resolve the correct ChromeDriver version automatically at runtime; no manual pinning required |
| Flaky tests due to page load timing | **Medium** | AngularJS single-page routing can cause `ElementNotInteractableException` if assertions fire before the DOM is ready | Apply explicit WebDriver waits (`WebDriverWait` + `ExpectedConditions`) on all dynamic elements; never use `Thread.sleep` |
| Shared public test environment | **Medium** | Other users may modify shared test data during testing | Use uniquely named test customers (e.g. prefixed with a timestamp). Verify test data state at the start of each test method |
| Docker image build failures in CI | **Medium** | A broken Dockerfile blocks the entire CI pipeline | Test Docker build locally before pushing. Pin base image and Java version in `Dockerfile` |
| Limited browser coverage | **Low** | Bugs in browsers outside Chrome may go undetected | Scope explicitly limited to Chrome. Document for future cross-browser regression cycles |

---

## 10. Environment Setup

All test execution will be conducted against the publicly available demo application. The setup
below applies both to local execution and to the Docker/GitHub Actions CI environment.

| Parameter | Details |
|---|---|
| Operating System | Windows 11 (local); Ubuntu (GitHub Actions runner) |
| Language & Runtime | Java 17 |
| Build Tool | Apache Maven 3.9+ |
| Browser | Google Chrome (latest stable version) |
| Driver Management | WebDriverManager (io.github.bonigarcia) — resolves ChromeDriver automatically |
| Test Framework | TestNG 7.x |
| Reporting | Allure Report with allure-testng adapter |
| Containerisation | Docker — base image `maven:3.9-eclipse-temurin-17` |
| CI/CD | GitHub Actions — workflow triggered on push to `main` |
| Test URL | https://www.globalsqa.com/angularJs-protractor/BankingProject/#/login |
| Screen Resolution | 1920 × 1080 (headless Chrome in CI) |
| Test Data | CSV files under `src/test/resources/testdata/` — valid and invalid inputs covering happy path, negative, and boundary scenarios |
| Environment Isolation | Each test method opens a fresh browser session via `@BeforeMethod`; cookies and session state are cleared automatically on teardown |
| Internet Dependency | Stable internet connection required; GitHub Actions has outbound access to the demo app URL |

---

## 11. Communication Plan

Communication throughout the testing lifecycle will be managed through the channels below. The
GitHub Actions CI dashboard and Allure report serve as the source of truth for test execution status.

| Channel | Purpose | Frequency | Participants |
|---|---|---|---|
| Daily Standup | Test progress, blockers, plan for the day | Daily | QA, Developers, Scrum Master |
| Slack / Teams Chat | Real-time defect clarifications, sharing Allure screenshots, getting unblocked | As needed | QA, Developers, Reviewer |
| GitHub Notifications | Automated updates on CI pipeline status (pass/fail per push) | Real-time | QA, Developers, Reviewer |
| Status Report | Summary of executed tests, pass/fail counts, open defects — generated from Allure Report | End of each phase | QA → PO + Reviewer |
| Sprint Review | Demo of Allure report, walk through of critical defects, review of test coverage | End of sprint | Full team + Reviewer |
