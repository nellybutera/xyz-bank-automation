package com.xyzbank.tests;

import com.xyzbank.base.BaseTest;
import com.xyzbank.pages.*;
import com.xyzbank.utils.CsvDataProvider;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Objects;

@Feature("Bank Manager")
public class ManagerTest extends BaseTest {

    private static final String ADD_CUSTOMER_CSV = "testdata/add_customer_data.csv";

    // -------------------------------------------------------------------------
    // TC-001: Add customer with valid alphabetic name and numeric post code
    // -------------------------------------------------------------------------
    @Test
    @Story("US-001 — Customer & Account Management")
    @Description("TC-001: Bank manager adds a customer with a valid alphabetic name and numeric post code. Expects success alert.")
    public void tc001_addCustomerWithValidData() {
        String firstName = "Valid" + System.currentTimeMillis();
        ManagerPage manager = new HomePage(driver).clickManagerLogin();
        AddCustomerPage addPage = manager.goToAddCustomer();

        String alertText = addPage.addCustomer(firstName, "AutoTest", "12345");

        Assert.assertTrue(alertText.contains("Customer added successfully"),
                "Expected success alert but got: " + alertText);
    }

    // -------------------------------------------------------------------------
    // TC-002 & TC-004 & TC-005: Data-driven add customer validation
    // Covers: numeric name (TC-002), alpha post code (TC-004), empty name (TC-005)
    // -------------------------------------------------------------------------
    @DataProvider(name = "addCustomerData")
    public Object[][] addCustomerData() {
        return CsvDataProvider.read(ADD_CUSTOMER_CSV);
    }

    @Test(dataProvider = "addCustomerData")
    @Story("US-001 — Customer & Account Management")
    @Description("Data-driven: validates that the Add Customer form enforces AC1 (alphabetic names, numeric post codes).")
    public void tc002_004_005_addCustomerValidation(
            String firstName, String lastName, String postCode,
            String shouldPass, String description) {

        ManagerPage manager = new HomePage(driver).clickManagerLogin();
        AddCustomerPage addPage = manager.goToAddCustomer();

        String alertText = addPage.addCustomer(firstName, lastName, postCode);
        boolean passed = alertText.contains("Customer added successfully");

        if (Boolean.parseBoolean(shouldPass)) {
            Assert.assertTrue(passed,
                    "[" + description + "] Expected success but got: " + alertText);
        } else {
            Assert.assertFalse(passed,
                    "[" + description + "] Input should have been rejected but was accepted. Alert: " + alertText);
        }
    }

    // -------------------------------------------------------------------------
    // TC-006: Bank manager opens account for an existing customer
    // -------------------------------------------------------------------------
    @Test
    @Story("US-001 — Customer & Account Management")
    @Description("TC-006: Bank manager creates an account for a customer. Expects account number in success alert.")
    public void tc006_openAccountForExistingCustomer() {
        String firstName = "AccTest" + System.currentTimeMillis();
        ManagerPage manager = new HomePage(driver).clickManagerLogin();
        manager.goToAddCustomer().addCustomer(firstName, "User", "99999");

        String alertText = manager.goToOpenAccount()
                                  .openAccount(firstName + " User", "Dollar");

        Assert.assertTrue(alertText.contains("Account created successfully"),
                "Expected account creation alert but got: " + alertText);
    }

    // -------------------------------------------------------------------------
    // TC-007: Open accounts in all three currencies for the same customer
    // -------------------------------------------------------------------------
    @Test
    @Story("US-001 — Customer & Account Management")
    @Description("TC-007: Bank manager creates Dollar, Pound, and Rupee accounts for the same customer.")
    public void tc007_openAccountsInAllCurrencies() {
        String firstName = "CurrTest" + System.currentTimeMillis();
        ManagerPage manager = new HomePage(driver).clickManagerLogin();
        manager.goToAddCustomer().addCustomer(firstName, "User", "11111");

        String fullName = firstName + " User";
        OpenAccountPage openPage = manager.goToOpenAccount();

        String dollar = openPage.openAccount(fullName, "Dollar");
        Assert.assertTrue(dollar.contains("Account created successfully"),
                "Dollar account not created: " + dollar);

        String pound = openPage.openAccount(fullName, "Pound");
        Assert.assertTrue(pound.contains("Account created successfully"),
                "Pound account not created: " + pound);

        String rupee = openPage.openAccount(fullName, "Rupee");
        Assert.assertTrue(rupee.contains("Account created successfully"),
                "Rupee account not created: " + rupee);
    }

    // -------------------------------------------------------------------------
    // TC-008: Customer access blocked before account creation, enabled after
    // -------------------------------------------------------------------------
    @Test
    @Story("US-001 — Customer & Account Management")
    @Description("TC-008: Customer cannot access banking features without an account; access enabled after manager creates one.")
    public void tc008_customerAccessBeforeAndAfterAccountCreation() {
        String firstName = "Access" + System.currentTimeMillis();
        ManagerPage manager = new HomePage(driver).clickManagerLogin();
        manager.goToAddCustomer().addCustomer(firstName, "Tester", "55555");

        // Customer should be in dropdown but have no account
        driver.get(BASE_URL);
        CustomerLoginPage loginPage = new HomePage(driver).clickCustomerLogin();
        AccountPage accountPage = loginPage.loginAs(firstName + " Tester");
        Assert.assertFalse(accountPage.hasAccount(),
                "Customer should not have an account yet");

        // Manager creates account
        driver.get(BASE_URL);
        new HomePage(driver).clickManagerLogin()
                            .goToOpenAccount()
                            .openAccount(firstName + " Tester", "Dollar");

        // Customer should now have an account
        driver.get(BASE_URL);
        loginPage = new HomePage(driver).clickCustomerLogin();
        accountPage = loginPage.loginAs(firstName + " Tester");
        Assert.assertTrue(accountPage.hasAccount(),
                "Customer should have an account after manager creates one");
    }

    // -------------------------------------------------------------------------
    // TC-009: Bank manager deletes an existing customer
    // -------------------------------------------------------------------------
    @Test
    @Story("US-001 — Customer & Account Management")
    @Description("TC-009: Bank manager deletes a customer. Customer should no longer appear in the list.")
    public void tc009_deleteCustomer() {
        String firstName = "Del" + System.currentTimeMillis();
        ManagerPage manager = new HomePage(driver).clickManagerLogin();
        manager.goToAddCustomer().addCustomer(firstName, "Target", "77777");

        CustomersPage customersPage = manager.goToCustomers();
        Assert.assertTrue(customersPage.isCustomerPresent(firstName + " Target"),
                "Customer should exist before deletion");

        customersPage.deleteCustomer(firstName + " Target");
        Assert.assertFalse(customersPage.isCustomerPresent(firstName + " Target"),
                "Customer should be removed after deletion");
    }

    // -------------------------------------------------------------------------
    // TC-011: Open Account rejects submission when no customer/currency selected
    // -------------------------------------------------------------------------
    @Test
    @Story("US-001 — Customer & Account Management")
    @Description("TC-011: Clicking Process without selecting customer or currency should not create an account.")
    public void tc011_openAccountWithNoSelectionRejected() {
        ManagerPage manager = new HomePage(driver).clickManagerLogin();
        OpenAccountPage openPage = manager.goToOpenAccount();

        // Process with no selections — expect either no alert or an error message
        try {
            String alertText = openPage.clickProcess();
            Assert.assertFalse(alertText.contains("Account created successfully"),
                    "Account should not be created without a customer/currency selection. Alert: " + alertText);
        } catch (org.openqa.selenium.TimeoutException e) {
            // No alert appeared — form was silently blocked, which is acceptable
        }
    }
}
