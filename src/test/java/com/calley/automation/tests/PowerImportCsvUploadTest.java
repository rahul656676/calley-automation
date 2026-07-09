package com.calley.automation.tests;

import com.aventstack.extentreports.Status;
import com.calley.automation.pages.CallListPowerImportPage;
import com.calley.automation.pages.LoginPage;
import com.calley.automation.utils.ConfigReader;
import com.calley.automation.utils.CsvDataReader;
import com.calley.automation.utils.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Scenario 4: Upload CSV of List (Call List -> Power Import)
 * Logs in, navigates to Power Import, names the list, selects the agent
 * created in AddAgentTest, uploads Sample_File_in_.csv, maps the
 * Name / Contact / Notes columns and imports.
 */
public class PowerImportCsvUploadTest extends BaseTest {

    @Test(priority = 4, description = "Upload contact list CSV via Call List > Power Import")
    public void testPowerImportCsvUpload() {
        ExtentReportManager.createTest("Power Import - CSV Upload",
                "Import Sample_File_in_.csv through Call List > Power Import");

        // Step 1: log in (skip if the session from an earlier test in this
        // suite run is still active, so Chrome doesn't close/reopen and ask
        // for login again)
        LoginPage loginPage = new LoginPage(driver);
        if (loginPage.isLoginSuccessful()) {
            ExtentReportManager.getTest().log(Status.INFO, "Already logged in - reusing existing session");
        } else {
            List<Map<String, String>> loginRows =
                    CsvDataReader.readCsvAsMaps("src/test/resources/testdata/LoginData.csv");
            Map<String, String> creds = loginRows.get(0);

            loginPage.open();
            loginPage.login(creds.get("Email"), creds.get("Password"));
            ExtentReportManager.getTest().log(Status.INFO, "Logged in to Calley dashboard");
        }

        // Step 2: resolve the CSV path and confirm it exists before upload
        String csvPath = new File(ConfigReader.get("call.list.csv")).getAbsolutePath();
        Assert.assertTrue(new File(csvPath).exists(), "Sample CSV file not found at: " + csvPath);
        ExtentReportManager.getTest().log(Status.INFO, "Using CSV file: " + csvPath);

        // Step 3: run the full Power Import flow
        List<Map<String, String>> agentRows =
                CsvDataReader.readCsvAsMaps("src/test/resources/testdata/AgentData.csv");
        String agentName = agentRows.get(0).get("AgentName");

        CallListPowerImportPage powerImportPage = new CallListPowerImportPage(driver);
        powerImportPage.importCallList("Automation Test List " + System.currentTimeMillis(), csvPath, agentName);
        ExtentReportManager.getTest().log(Status.INFO, "Submitted Power Import with agent: " + agentName);

        boolean importSuccessful = powerImportPage.isImportSuccessful();
        if (!importSuccessful) {
            ExtentReportManager.getTest().log(Status.WARNING, "Import success toast not detected.");
        } else {
            ExtentReportManager.getTest().log(Status.INFO,
                    "Imported record count: " + powerImportPage.getImportedRecordCount());
        }

        Assert.assertTrue(importSuccessful, "Expected the CSV list import to complete successfully");
    }
}
