package com.xyzbank.tests;

import com.xyzbank.base.BaseTest;
import com.xyzbank.pages.*;
import com.xyzbank.utils.CsvDataProvider;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * Customer tests — TC-003, TC-010, TC-012 to TC-020.
 *
 * Each test creates its own customer + account in the current browser session
 * so test data always exists in the same session that uses it.
 */
@Feature("Customer")
public class CustomerTest extends BaseTest {

    private static final String TRANSACTION_CSV = "testdata/transaction_data.csv";

    // -------------------------------------------------------------------------
    // TC-014: Deposit positive amount — balance updates (AC5)
    // -------------------------------------------------------------------------
    @Test
    @Story("US-002 — Customer Banking Experience")
    @Description("TC-014: Customer deposits $1000. Balance should increase by the deposited amount. (AC5)")
    public void tc014_depositPositiveAmount() {
        AccountPage account = setupAndLogin();
        double before = account.getBalance();

        account.goToDeposit().deposit("1000");

        Assert.assertEquals(account.getBalance(), before + 1000,
                "Balance should increase by deposited amount");
    }

    // -------------------------------------------------------------------------
    // TC-015: Cannot deposit negative amount (AC5 — known defect)
    // -------------------------------------------------------------------------
    @Test
    @Story("US-002 — Customer Banking Experience")
    @Description("TC-015: Deposit of -100 should be rejected. Balance should not change. (AC5 — known defect)")
    public void tc015_cannotDepositNegativeAmount() {
        AccountPage account = setupAndLogin();
        account.goToDeposit().deposit("1000");
        double before = account.getBalance();

        account.goToDeposit().deposit("-100");

        Assert.assertEquals(account.getBalance(), before,
                "Balance should NOT change after negative deposit attempt");
    }

    // -------------------------------------------------------------------------
    // TC-016: Cannot deposit zero (AC5 — known defect)
    // -------------------------------------------------------------------------
    @Test
    @Story("US-002 — Customer Banking Experience")
    @Description("TC-016: Deposit of 0 should be rejected. Balance should not change. (AC5 — known defect)")
    public void tc016_cannotDepositZero() {
        AccountPage account = setupAndLogin();
        account.goToDeposit().deposit("1000");
        double before = account.getBalance();

        account.goToDeposit().deposit("0");

        Assert.assertEquals(account.getBalance(), before,
                "Balance should NOT change after zero deposit attempt");
    }

    // -------------------------------------------------------------------------
    // TC-017: Withdraw positive amount with sufficient balance (AC6)
    // -------------------------------------------------------------------------
    @Test
    @Story("US-002 — Customer Banking Experience")
    @Description("TC-017: Customer withdraws $500 from a $1000 balance. Balance should become $500. (AC6)")
    public void tc017_withdrawPositiveAmountWithSufficientBalance() {
        AccountPage account = setupAndLogin();
        account.goToDeposit().deposit("1000");

        account.goToWithdraw().withdraw("500");

        Assert.assertEquals(account.getBalance(), 500.0,
                "Balance should be $500 after withdrawing $500 from $1000");
    }

    // -------------------------------------------------------------------------
    // TC-018: Withdrawal rejected when exceeds balance (AC6)
    // -------------------------------------------------------------------------
    @Test
    @Story("US-002 — Customer Banking Experience")
    @Description("TC-018: Withdrawal of $99999 (exceeds balance) should be rejected. Balance unchanged. (AC6)")
    public void tc018_withdrawalRejectedWhenExceedsBalance() {
        AccountPage account = setupAndLogin();
        account.goToDeposit().deposit("1000");
        double before = account.getBalance();

        account.goToWithdraw().withdraw("99999");

        Assert.assertEquals(account.getBalance(), before,
                "Balance should not change when withdrawal exceeds balance");
    }

    // -------------------------------------------------------------------------
    // TC-003: Withdrawal rejects negative amount (AC6 — known defect)
    // -------------------------------------------------------------------------
    @Test
    @Story("US-002 — Customer Banking Experience")
    @Description("TC-003: Withdrawal of -100 should be rejected. Balance unchanged. (AC6 — known defect)")
    public void tc003_withdrawRejectsNegativeAmount() {
        AccountPage account = setupAndLogin();
        account.goToDeposit().deposit("1000");
        double before = account.getBalance();

        account.goToWithdraw().withdraw("-100");

        Assert.assertEquals(account.getBalance(), before,
                "Balance should NOT change after negative withdrawal attempt");
    }

    // -------------------------------------------------------------------------
    // TC-010: Withdraw exact balance, leaving zero (AC6)
    // -------------------------------------------------------------------------
    @Test
    @Story("US-002 — Customer Banking Experience")
    @Description("TC-010: Customer withdraws the exact account balance. Balance should be 0. (AC6)")
    public void tc010_withdrawExactBalanceLeavingZero() {
        AccountPage account = setupAndLogin();
        account.goToDeposit().deposit("500");

        account.goToWithdraw().withdraw("500");

        Assert.assertEquals(account.getBalance(), 0.0,
                "Balance should be exactly 0 after withdrawing full amount");
    }

    // -------------------------------------------------------------------------
    // TC-013: Withdrawal rejected on zero-balance account (AC6)
    // -------------------------------------------------------------------------
    @Test
    @Story("US-002 — Customer Banking Experience")
    @Description("TC-013: Withdrawal from a zero-balance account should be rejected. Balance stays 0. (AC6)")
    public void tc013_withdrawRejectedOnZeroBalance() {
        AccountPage account = setupAndLogin();
        Assert.assertEquals(account.getBalance(), 0.0,
                "Prerequisite: account balance must be 0");

        account.goToWithdraw().withdraw("100");

        Assert.assertEquals(account.getBalance(), 0.0,
                "Balance should remain 0 when withdrawal is rejected on zero balance");
    }

    // -------------------------------------------------------------------------
    // TC-012: Switching accounts updates balance and currency (AC2)
    // -------------------------------------------------------------------------
    @Test
    @Story("US-002 — Customer Banking Experience")
    @Description("TC-012: Selecting a different account from the dropdown updates the displayed balance. (AC2)")
    public void tc012_switchingAccountsUpdatesBalance() {
        AccountPage account = setupAndLogin();  // setupAndLogin opens Dollar + Pound accounts
        account.goToDeposit().deposit("200");
        double dollarBalance = account.getBalance();

        SoftAssert soft = new SoftAssert();
        int accountCount = driver.findElements(By.cssSelector("select#accountSelect option")).size();
        soft.assertTrue(accountCount >= 2,
                "Customer should have at least 2 accounts to test switching");

        if (accountCount >= 2) {
            new Select(driver.findElement(By.cssSelector("select#accountSelect"))).selectByIndex(1);
            double secondBalance = account.getBalance();
            soft.assertNotEquals(dollarBalance, secondBalance,
                    "Switching to a different account should show a different balance");
        }
        soft.assertAll();
    }

    // -------------------------------------------------------------------------
    // TC-019: Deposits and withdrawals appear in transactions table (AC4)
    // -------------------------------------------------------------------------
    @Test
    @Story("US-002 — Customer Banking Experience")
    @Description("TC-019: After a deposit and withdrawal, both appear in the Transactions tab. (AC4)")
    public void tc019_transactionsAppearInTable() {
        AccountPage account = setupAndLogin();
        account.goToDeposit().deposit("500");
        account.goToWithdraw().withdraw("200");

        int count = account.goToTransactions().getTransactionCount();

        Assert.assertTrue(count >= 2,
                "Expected at least 2 transaction rows but found: " + count);
    }

    // -------------------------------------------------------------------------
    // TC-020: Customer cannot edit or delete transaction records (AC7 — known defect)
    // -------------------------------------------------------------------------
    @Test
    @Story("US-002 — Customer Banking Experience")
    @Description("TC-020: The Transactions page must not expose controls to alter history. Reset button presence violates AC7. (known defect)")
    public void tc020_customerCannotAlterTransactionHistory() {
        AccountPage account = setupAndLogin();
        account.goToDeposit().deposit("300");

        TransactionsPage txPage = account.goToTransactions();
        Assert.assertTrue(txPage.getTransactionCount() > 0,
                "Prerequisite: there must be at least one transaction");

        Assert.assertFalse(txPage.isResetButtonPresent(),
                "Reset button should not be available — customers must not alter transaction history (AC7)");
    }

    // -------------------------------------------------------------------------
    // Data-driven transaction edge cases (AC5 & AC6)
    // -------------------------------------------------------------------------
    @DataProvider(name = "transactionData")
    public Object[][] transactionData() {
        return CsvDataProvider.read(TRANSACTION_CSV);
    }

    @Test(dataProvider = "transactionData")
    @Story("US-002 — Customer Banking Experience")
    @Description("Data-driven: deposit and withdrawal edge cases from CSV (AC5 & AC6).")
    public void tc_datadriven_transactionValidation(
            String amount, String type, String shouldSucceed, String description) {

        AccountPage account = setupAndLogin();
        // Give withdrawal tests a sufficient starting balance
        if ("withdraw".equals(type) && !amount.startsWith("-") && !amount.equals("0")) {
            account.goToDeposit().deposit("2000");
        }
        double before = account.getBalance();

        if ("deposit".equals(type)) {
            account.goToDeposit().deposit(amount);
        } else {
            account.goToWithdraw().withdraw(amount);
        }

        double after = account.getBalance();
        boolean changed = after != before;

        if (Boolean.parseBoolean(shouldSucceed)) {
            Assert.assertTrue(changed,
                    "[" + description + "] Transaction should have succeeded but balance did not change");
        } else {
            Assert.assertFalse(changed,
                    "[" + description + "] Transaction should have been rejected but balance changed. Before: "
                            + before + " After: " + after);
        }
    }

    // -------------------------------------------------------------------------
    // Helper: create a fresh customer + 2 accounts in this browser session,
    // then log in and return the AccountPage
    // -------------------------------------------------------------------------
    @Step("Create test customer and login")
    private AccountPage setupAndLogin() {
        String name = "Test" + System.currentTimeMillis();
        ManagerPage manager = new HomePage(driver).clickManagerLogin();
        manager.goToAddCustomer().addCustomer(name, "User", "12345");
        manager.goToOpenAccount().openAccount(name + " User", "Dollar");
        manager.goToOpenAccount().openAccount(name + " User", "Pound");
        driver.get(BASE_URL);
        AccountPage account = new HomePage(driver).clickCustomerLogin().loginAs(name + " User");
        // With multiple accounts the app doesn't auto-select; wait for the
        // dropdown to be visible, then force-select the first account.
        account.selectFirstAccount();
        return account;
    }
}
