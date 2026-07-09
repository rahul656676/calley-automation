package com.calley.automation.tests;

import com.aventstack.extentreports.Status;
import com.calley.automation.pages.LoginPage;
import com.calley.automation.utils.ConfigReader;
import com.calley.automation.utils.CsvDataReader;
import com.calley.automation.utils.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 * Scenario 2: User Login
 * Logs in with credentials of an already-registered Calley Teams account
 * and validates that the dashboard loads successfully.
 */
public class LoginTest extends BaseTest {

    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        List<Map<String, String>> rows = CsvDataReader.readCsvAsMaps("src/test/resources/testdata/LoginData.csv");
        return CsvDataReader.toDataProviderArray(rows);
    }

    @Test(dataProvider = "loginData", priority = 2,
          description = "Log in with valid Calley Teams credentials")
    public void testUserLogin(Map<String, String> loginData) {
        ExtentReportManager.createTest("User Login", "Login with " + loginData.get("Email"));

        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        ExtentReportManager.getTest().log(Status.INFO, "Opened login page: " + ConfigReader.loginUrl());

        loginPage.login(loginData.get("Email"), loginData.get("Password"));
        ExtentReportManager.getTest().log(Status.INFO, "Submitted login form");

        boolean isLoggedIn = loginPage.isLoginSuccessful();
        if (!isLoggedIn) {
            ExtentReportManager.getTest().log(Status.WARNING,
                    "Dashboard marker not found. Page error (if any): " + loginPage.getErrorMessage());
        }

        Assert.assertTrue(isLoggedIn, "Expected login to succeed for " + loginData.get("Email")
                + " but dashboard was not detected. Error: " + loginPage.getErrorMessage());
    }
}
