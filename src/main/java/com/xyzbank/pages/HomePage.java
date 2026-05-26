package com.xyzbank.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {

    private static final By MANAGER_LOGIN_BTN = By.xpath("//button[contains(text(),'Bank Manager Login')]");
    private static final By CUSTOMER_LOGIN_BTN = By.xpath("//button[contains(text(),'Customer Login')]");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    @Step("Click Bank Manager Login")
    public ManagerPage clickManagerLogin() {
        click(MANAGER_LOGIN_BTN);
        return new ManagerPage(driver);
    }

    @Step("Click Customer Login")
    public CustomerLoginPage clickCustomerLogin() {
        click(CUSTOMER_LOGIN_BTN);
        return new CustomerLoginPage(driver);
    }
}
