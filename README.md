# XYZ Bank Automation

Selenium WebDriver II lab — automated test suite for the [XYZ Bank demo app](https://www.globalsqa.com/angularJs-protractor/BankingProject/#/login).

## Stack

| Tool | Version | Purpose |
|---|---|---|
| Java | 17 | Language |
| Maven | 3.9+ | Build & dependency management |
| Selenium WebDriver | 4.18 | Browser automation |
| TestNG | 7.10 | Test runner |
| WebDriverManager | 5.7 | Auto-resolves ChromeDriver |
| Allure | 2.25 | Test reporting |
| Docker | — | Containerised execution |
| GitHub Actions | — | CI/CD pipeline |

## Project Structure

```
src/
├── main/java/com/xyzbank/pages/   # Page Object Model classes
│   ├── BasePage.java
│   ├── HomePage.java
│   ├── ManagerPage.java
│   ├── AddCustomerPage.java
│   ├── OpenAccountPage.java
│   ├── CustomersPage.java
│   ├── CustomerLoginPage.java
│   ├── AccountPage.java
│   ├── DepositPage.java
│   ├── WithdrawPage.java
│   └── TransactionsPage.java
└── test/java/com/xyzbank/
    ├── base/BaseTest.java          # WebDriver setup/teardown + screenshot on failure
    ├── tests/
    │   ├── ManagerTest.java        # TC-001 to TC-011 (Bank Manager flows)
    │   └── CustomerTest.java       # TC-003, TC-010, TC-012 to TC-020 (Customer flows)
    └── utils/CsvDataProvider.java  # CSV-based @DataProvider helper
```

## Test Coverage

31 tests across 20 planned scenarios.

| Test Class | Tests | Notes |
|---|---|---|
| ManagerTest | 12 | Add customer, open account, delete customer, access control |
| CustomerTest | 19 | Deposit, withdrawal, transactions, account switching |

**Known defects (intentional failures):**
- `tc002_004_005` — App accepts numeric names and alphabetic post codes (AC1 violation)
- `tc020` — Reset button exposed on Transactions page (AC7 violation)

## Run Locally

**Prerequisites:** Java 17, Maven, Google Chrome

```bash
mvn test
```

Generate and open the Allure report after a run:

```bash
mvn allure:report
mvn allure:serve
```

## Run with Docker

```bash
docker build -t xyz-bank-automation .
docker run xyz-bank-automation
```

## CI / Allure Report

Every push to `main` triggers the GitHub Actions pipeline. The Allure report is published to GitHub Pages after each run:

**[View Allure Report](https://nellybutera.github.io/xyz-bank-automation/)**

## Test Data

CSV files under `src/test/resources/testdata/`:

| File | Used by |
|---|---|
| `add_customer_data.csv` | `tc002_004_005_addCustomerValidation` — valid and invalid customer inputs |
| `transaction_data.csv` | `tc_datadriven_transactionValidation` — deposit and withdrawal edge cases |
