package com.xyzbank.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CustomerLoginPage extends BasePage {

    private static final By CUSTOMER_SELECT = By.id("userSelect");
    // The form uses ng-submit, so the button has no ng-click — match by text
    private static final By LOGIN_BTN = By.xpath("//button[normalize-space(text())='Login']");

    public CustomerLoginPage(WebDriver driver) {
        super(driver);
    }

    @Step("Select customer: {customerName}")
    public CustomerLoginPage selectCustomer(String customerName) {
        selectByVisibleText(CUSTOMER_SELECT, customerName);
        return this;
    }

    @Step("Click Login")
    public AccountPage clickLogin() {
        click(LOGIN_BTN);
        return new AccountPage(driver);
    }

    @Step("Login as: {customerName}")
    public AccountPage loginAs(String customerName) {
        selectCustomer(customerName);
        return clickLogin();
    }

    public boolean isCustomerInDropdown(String customerName) {
        return !driver.findElements(
                By.xpath("//select[@id='userSelect']/option[normalize-space(text())='" + customerName + "']")
        ).isEmpty();
    }
}
