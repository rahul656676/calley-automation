package com.calley.automation.tests;

import com.aventstack.extentreports.Status;
import com.calley.automation.pages.AgentPage;
import com.calley.automation.pages.LoginPage;
import com.calley.automation.utils.CsvDataReader;
import com.calley.automation.utils.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class AddAgentTest extends BaseTest {

    @DataProvider(name = "agentData")
    public Object[][] agentData() {

        List<Map<String, String>> rows =
                CsvDataReader.readCsvAsMaps("src/test/resources/testdata/AgentData.csv");

        return CsvDataReader.toDataProviderArray(rows);
    }

    private void loginFirst() {

        LoginPage loginPage = new LoginPage(driver);

        if (loginPage.isLoginSuccessful()) {
            return;
        }

        List<Map<String, String>> loginRows =
                CsvDataReader.readCsvAsMaps("src/test/resources/testdata/LoginData.csv");

        Map<String, String> creds = loginRows.get(0);

        loginPage.open();

        loginPage.login(
                creds.get("Email"),
                creds.get("Password")
        );

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        System.out.println("Current URL After Login : " + driver.getCurrentUrl());

        // Wait for dashboard to load completely before proceeding
        new org.openqa.selenium.support.ui.WebDriverWait(driver, Duration.ofSeconds(10))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("dashboard"));
    }

    @Test(dataProvider = "agentData", priority = 3)
    public void testAddAgent(Map<String, String> agentData) {

        ExtentReportManager.createTest(
                "Add Agent",
                "Add Agent : " + agentData.get("AgentName"));

        loginFirst();

        System.out.println("Before Agent Page : " + driver.getCurrentUrl());

        AgentPage agentPage = new AgentPage(driver);

        agentPage.addAgent(agentData);

        ExtentReportManager.getTest().log(
                Status.INFO,
                "Agent Submitted");

        Assert.assertTrue(
                agentPage.isAgentAddedSuccessfully()
                        || agentPage.isAgentPresentInGrid(agentData.get("AgentName"))
        );
    }
}