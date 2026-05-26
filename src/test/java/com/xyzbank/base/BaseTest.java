package com.xyzbank.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;
import java.util.logging.Logger;

public class BaseTest {

    protected static final String BASE_URL =
            "https://www.globalsqa.com/angularJs-protractor/BankingProject/#/login";

    protected WebDriver driver;

    private static final Logger log = Logger.getLogger(BaseTest.class.getName());

    protected WebDriver createDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        boolean headless = Boolean.parseBoolean(System.getenv().getOrDefault("CI", "false"))
                        || Boolean.parseBoolean(System.getProperty("headless", "false"));
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");
        }
        options.addArguments("--log-level=3");
        WebDriver d = new ChromeDriver(options);
        d.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        d.manage().window().maximize();
        return d;
    }

    @BeforeMethod
    public void setUp() {
        driver = createDriver();
        driver.get(BASE_URL);
        log.info("Browser opened — navigated to " + BASE_URL);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            log.warning("Test FAILED: " + result.getName() + " — capturing screenshot");
            captureScreenshot(result.getName());
        }
        if (driver != null) {
            driver.quit();
            log.info("Browser closed after: " + result.getName());
        }
    }

    @Attachment(value = "Screenshot — {testName}", type = "image/png")
    private byte[] captureScreenshot(String testName) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}
