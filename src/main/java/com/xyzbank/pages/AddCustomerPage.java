package com.xyzbank.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AddCustomerPage extends BasePage {

    private static final By FIRST_NAME  = By.xpath("//input[@ng-model='fName']");
    private static final By LAST_NAME   = By.xpath("//input[@ng-model='lName']");
    private static final By POST_CODE   = By.xpath("//input[@ng-model='postCd']");
    private static final By ADD_BTN     = By.xpath("//button[@type='submit']");

    public AddCustomerPage(WebDriver driver) {
        super(driver);
    }

    @Step("Enter first name: {firstName}")
    public AddCustomerPage enterFirstName(String firstName) {
        type(FIRST_NAME, firstName);
        return this;
    }

    @Step("Enter last name: {lastName}")
    public AddCustomerPage enterLastName(String lastName) {
        type(LAST_NAME, lastName);
        return this;
    }

    @Step("Enter post code: {postCode}")
    public AddCustomerPage enterPostCode(String postCode) {
        type(POST_CODE, postCode);
        return this;
    }

    @Step("Submit Add Customer form")
    public String submit() {
        click(ADD_BTN);
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String message = alert.getText();
            alert.accept();
            return message;
        } catch (org.openqa.selenium.TimeoutException e) {
            // No alert — form blocked by HTML5 validation (e.g. empty required field)
            return "REJECTED";
        }
    }

    @Step("Add customer: {firstName} {lastName}, postcode {postCode}")
    public String addCustomer(String firstName, String lastName, String postCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostCode(postCode);
        return submit();
    }

    public boolean isAddButtonEnabled() {
        return waitForVisible(ADD_BTN).isEnabled();
    }
}
