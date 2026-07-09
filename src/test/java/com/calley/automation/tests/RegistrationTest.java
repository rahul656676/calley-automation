package com.calley.automation.tests;

import com.aventstack.extentreports.Status;
import com.calley.automation.pages.RegistrationPage;
import com.calley.automation.utils.ConfigReader;
import com.calley.automation.utils.CsvDataReader;
import com.calley.automation.utils.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 * Scenario 1: User Registration
 * Navigates to the registration page, fills the form from CSV test data,
 * selects the "Calley Teams" plan, submits, and validates success.
 */
public class RegistrationTest extends BaseTest {

    @DataProvider(name = "userData")
    public Object[][] userData() {
        List<Map<String, String>> rows = CsvDataReader.readCsvAsMaps(ConfigReader.get("user.data.file"));
        return CsvDataReader.toDataProviderArray(rows);
    }

    @Test(dataProvider = "userData", priority = 1,
          description = "Register a new user and select the Calley Teams plan")
    public void testUserRegistration(Map<String, String> userData) {
        ExtentReportManager.createTest("User Registration",
                "Register user " + userData.get("Email") + " with plan " + userData.get("Plan"));

        RegistrationPage registrationPage = new RegistrationPage(driver);
        registrationPage.open();
        ExtentReportManager.getTest().log(Status.INFO, "Opened registration page: " + ConfigReader.registrationUrl());

        registrationPage.registerUser(userData);
        ExtentReportManager.getTest().log(Status.INFO, "Submitted registration form for " + userData.get("Email"));

        boolean isSuccess = registrationPage.isRegistrationSuccessful();
        if (!isSuccess) {
            ExtentReportManager.getTest().log(Status.WARNING,
                    "Success banner not found. Page error (if any): " + registrationPage.getErrorMessage());
        }

        Assert.assertTrue(isSuccess, "Expected registration to succeed for " + userData.get("Email")
                + " but success confirmation was not displayed. Error: " + registrationPage.getErrorMessage());
    }
}
