package com.xyzbank.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class AccountPage extends BasePage {

    // ng-click attributes are the most stable locators in this AngularJS app
    private static final By ACCOUNT_SELECT   = By.id("accountSelect");
    private static final By DEPOSIT_TAB      = By.cssSelector("button[ng-click='deposit()']");
    private static final By WITHDRAW_TAB     = By.cssSelector("button[ng-click='withdrawl()']");
    private static final By TRANSACTIONS_TAB = By.cssSelector("button[ng-click='transactions()']");
    private static final By LOGOUT_BTN       = By.cssSelector("button[ng-click='byebye()']");

    // DOM: "Account Number : [strong] , Balance : [strong] , Currency : [strong]"
    // "Balance :" is plain text — target the 2nd ng-binding strong in the account info div.
    private static final By BALANCE = By.xpath(
            "//div[@ng-hide='noAccount']//strong[@class='ng-binding'][2]");

    public AccountPage(WebDriver driver) {
        super(driver);
    }

    @Step("Select first account in dropdown")
    public AccountPage selectFirstAccount() {
        new Select(waitForVisible(ACCOUNT_SELECT)).selectByIndex(0);
        return this;
    }

    @Step("Get current balance")
    public double getBalance() {
        return Double.parseDouble(getText(BALANCE));
    }

    @Step("Select account: {accountNo}")
    public AccountPage selectAccount(String accountNo) {
        selectByVisibleText(ACCOUNT_SELECT, accountNo);
        return this;
    }

    public boolean hasAccount() {
        return isElementPresent(ACCOUNT_SELECT) &&
               !driver.findElements(By.cssSelector("select#accountSelect option")).isEmpty();
    }

    // Selector for the deposit form — used as a transition anchor.
    private static final By DEPOSIT_FORM = By.cssSelector("form[ng-submit='deposit()']");

    @Step("Go to Deposit tab")
    public DepositPage goToDeposit() {
        click(DEPOSIT_TAB);
        // Wait for the deposit form to be present in the ui-view
        waitForVisible(DEPOSIT_FORM);
        return new DepositPage(driver);
    }

    @Step("Go to Withdrawal tab")
    public WithdrawPage goToWithdraw() {
        click(WITHDRAW_TAB);
        // ui-router replaces the view async; wait until the deposit form is gone
        // so we don't interact with it instead of the withdrawal form
        wait.until(ExpectedConditions.invisibilityOfElementLocated(DEPOSIT_FORM));
        return new WithdrawPage(driver);
    }

    @Step("Go to Transactions tab")
    public TransactionsPage goToTransactions() {
        click(TRANSACTIONS_TAB);
        // The Back button is unique to the transactions view; wait for it to
        // confirm the ui-router has replaced the form content.
        waitForVisible(By.xpath("//button[normalize-space(text())='Back']"));
        return new TransactionsPage(driver);
    }

    @Step("Logout")
    public void logout() {
        click(LOGOUT_BTN);
    }
}
