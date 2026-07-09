package com.calley.automation.tests;

import com.aventstack.extentreports.Status;
import com.calley.automation.base.DriverManager;
import com.calley.automation.utils.ExtentReportManager;
import com.calley.automation.utils.ScreenshotUtils;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

/**
 * Every test class extends this. Handles:
 *  - one WebDriver instance per test method (thread-safe via DriverManager)
 *  - ExtentReports test creation / pass-fail logging
 *  - automatic screenshot capture and attachment on failure
 */
public class BaseTest {

    protected WebDriver driver;

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        ExtentReportManager.getInstance();
        // Single browser instance is opened once for the whole suite so the
        // logged-in session carries over from LoginTest into AddAgentTest
        // and PowerImportCsvUploadTest instead of closing/reopening Chrome
        // and asking to log in again for every test class.
        driver = DriverManager.getDriver();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        driver = DriverManager.getDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            String screenshotPath = ScreenshotUtils.capture(driver, result.getMethod().getMethodName());
            if (ExtentReportManager.getTest() != null) {
                ExtentReportManager.getTest().log(Status.FAIL, "Test Failed: " + result.getThrowable());
                if (screenshotPath != null) {
                    try {
                        ExtentReportManager.getTest().addScreenCaptureFromPath(screenshotPath);
                    } catch (Exception ignored) {
                        // report screenshot embedding is best-effort only
                    }
                }
            }
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            if (ExtentReportManager.getTest() != null) {
                ExtentReportManager.getTest().log(Status.PASS, result.getMethod().getMethodName() + " passed");
            }
        }
        // NOTE: driver is intentionally NOT quit here - it stays open for
        // the entire suite. It is closed once in afterSuite() below.
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        DriverManager.quitDriver();
        ExtentReportManager.flush();
    }
}
