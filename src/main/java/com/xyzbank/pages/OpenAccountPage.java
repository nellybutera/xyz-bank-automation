package com.xyzbank.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class OpenAccountPage extends BasePage {

    private static final By CUSTOMER_SELECT  = By.id("userSelect");
    private static final By CURRENCY_SELECT  = By.xpath("//select[@ng-model='currency']");
    private static final By PROCESS_BTN      = By.xpath("//button[@type='submit']");

    public OpenAccountPage(WebDriver driver) {
        super(driver);
    }

    @Step("Select customer: {customerName}")
    public OpenAccountPage selectCustomer(String customerName) {
        selectByVisibleText(CUSTOMER_SELECT, customerName);
        return this;
    }

    @Step("Select currency: {currency}")
    public OpenAccountPage selectCurrency(String currency) {
        selectByVisibleText(CURRENCY_SELECT, currency);
        return this;
    }

    @Step("Click Process")
    public String clickProcess() {
        click(PROCESS_BTN);
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        String message = alert.getText();
        alert.accept();
        return message;
    }

    @Step("Open account for {customerName} in {currency}")
    public String openAccount(String customerName, String currency) {
        selectCustomer(customerName);
        selectCurrency(currency);
        return clickProcess();
    }
}
