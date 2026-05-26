package com.xyzbank.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ManagerPage extends BasePage {

    private static final By ADD_CUSTOMER_TAB  = By.xpath("//button[contains(text(),'Add Customer')]");
    private static final By OPEN_ACCOUNT_TAB  = By.xpath("//button[contains(text(),'Open Account')]");
    private static final By CUSTOMERS_TAB     = By.xpath("//button[contains(text(),'Customers')]");

    public ManagerPage(WebDriver driver) {
        super(driver);
    }

    @Step("Go to Add Customer tab")
    public AddCustomerPage goToAddCustomer() {
        click(ADD_CUSTOMER_TAB);
        return new AddCustomerPage(driver);
    }

    @Step("Go to Open Account tab")
    public OpenAccountPage goToOpenAccount() {
        click(OPEN_ACCOUNT_TAB);
        return new OpenAccountPage(driver);
    }

    @Step("Go to Customers tab")
    public CustomersPage goToCustomers() {
        click(CUSTOMERS_TAB);
        return new CustomersPage(driver);
    }
}
