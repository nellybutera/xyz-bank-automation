package com.xyzbank.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class WithdrawPage extends BasePage {

    private static final By AMOUNT_INPUT = By.cssSelector("input[ng-model='amount']");
    private static final By WITHDRAW_BTN = By.cssSelector("button[type='submit']");
    private static final By MESSAGE      = By.cssSelector("span.error.ng-binding");

    public WithdrawPage(WebDriver driver) {
        super(driver);
    }

    @Step("Enter withdrawal amount: {amount}")
    public WithdrawPage enterAmount(String amount) {
        type(AMOUNT_INPUT, amount);
        return this;
    }

    @Step("Click Withdraw button")
    public WithdrawPage clickWithdraw() {
        click(WITHDRAW_BTN);
        return this;
    }

    @Step("Withdraw: {amount}")
    public WithdrawPage withdraw(String amount) {
        enterAmount(amount);
        clickWithdraw();
        return this;
    }

    public String getMessage() {
        return isElementPresent(MESSAGE) ? getText(MESSAGE) : "";
    }
}
