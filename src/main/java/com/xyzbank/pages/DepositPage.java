package com.xyzbank.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DepositPage extends BasePage {

    private static final By AMOUNT_INPUT = By.cssSelector("input[ng-model='amount']");
    private static final By DEPOSIT_BTN  = By.cssSelector("button[type='submit']");
    private static final By MESSAGE      = By.cssSelector("span.error.ng-binding");

    public DepositPage(WebDriver driver) {
        super(driver);
    }

    @Step("Enter deposit amount: {amount}")
    public DepositPage enterAmount(String amount) {
        type(AMOUNT_INPUT, amount);
        return this;
    }

    @Step("Click Deposit button")
    public DepositPage clickDeposit() {
        click(DEPOSIT_BTN);
        return this;
    }

    @Step("Deposit: {amount}")
    public DepositPage deposit(String amount) {
        enterAmount(amount);
        clickDeposit();
        return this;
    }

    public String getMessage() {
        return isElementPresent(MESSAGE) ? getText(MESSAGE) : "";
    }
}
