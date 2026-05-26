package com.xyzbank.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class TransactionsPage extends BasePage {

    private static final By TRANSACTION_ROWS = By.xpath("//table/tbody/tr");
    private static final By RESET_BTN        = By.xpath("//button[text()='Reset']");
    private static final By BACK_BTN         = By.xpath("//button[text()='Back']");

    public TransactionsPage(WebDriver driver) {
        super(driver);
    }

    @Step("Get transaction count")
    public int getTransactionCount() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(TRANSACTION_ROWS));
            List<WebElement> rows = driver.findElements(TRANSACTION_ROWS);
            return (int) rows.stream().filter(r -> !r.getText().isBlank()).count();
        } catch (org.openqa.selenium.TimeoutException e) {
            // The sDate date filter initializes to the current time, hiding transactions
            // recorded seconds ago. Fall back to reading the raw Angular scope array.
            try {
                // When the sDate filter is active all tr rows are comment nodes — no [ng-repeat]
                // elements exist. Walk up the scope chain from the table (always present) to
                // find the controller scope that owns the raw transactions array.
                Object result = ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                    "try {" +
                    "  var el = document.querySelector('table') || document.body;" +
                    "  var sc = angular.element(el).scope();" +
                    "  while (sc) {" +
                    "    if (sc.transactions !== undefined) return sc.transactions.length;" +
                    "    sc = sc.$parent;" +
                    "  }" +
                    "  return 0;" +
                    "} catch(e) { return 0; }"
                );
                return result instanceof Long ? ((Long) result).intValue() : 0;
            } catch (Exception jsEx) {
                return 0;
            }
        }
    }

    @Step("Click Reset button")
    public TransactionsPage clickReset() {
        click(RESET_BTN);
        return this;
    }

    @Step("Click Back button")
    public AccountPage clickBack() {
        click(BACK_BTN);
        return new AccountPage(driver);
    }

    public boolean isResetButtonPresent() {
        return isElementPresent(RESET_BTN);
    }

    public boolean isTransactionTableEmpty() {
        return getTransactionCount() == 0;
    }

    public List<WebElement> getRows() {
        return driver.findElements(TRANSACTION_ROWS);
    }
}
