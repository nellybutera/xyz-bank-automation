package com.xyzbank.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CustomersPage extends BasePage {

    private static final By SEARCH_INPUT = By.cssSelector("input[ng-model='searchCustomer']");

    public CustomersPage(WebDriver driver) {
        super(driver);
    }

    @Step("Search for customer: {firstName}")
    private void search(String firstName) {
        type(SEARCH_INPUT, firstName);
    }

    @Step("Check if customer exists: {name}")
    public boolean isCustomerPresent(String name) {
        String firstName = name.split(" ")[0];
        search(firstName);
        By cellLocator = By.xpath("//table//td[contains(text(),'" + firstName + "')]");
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(cellLocator));
            return true;
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }

    @Step("Delete customer: {name}")
    public void deleteCustomer(String name) {
        String firstName = name.split(" ")[0];
        search(firstName);
        // Wait for the filter to settle: once the customer's cell is visible,
        // only one Delete button remains in the filtered table.
        waitForVisible(By.xpath("//table//td[contains(text(),'" + firstName + "')]"));
        waitForClickable(By.cssSelector("button[ng-click='deleteCust(cust)']")).click();
    }

    public int getCustomerCount() {
        return driver.findElements(By.cssSelector("table.table tbody tr")).size();
    }
}
