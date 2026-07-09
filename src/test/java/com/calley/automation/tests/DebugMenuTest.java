package com.calley.automation.tests;

import com.calley.automation.pages.LoginPage;
import com.calley.automation.utils.CsvDataReader;
import org.testng.annotations.Test;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class DebugMenuTest extends BaseTest {

    @Test
    public void dumpPageSource() throws Exception {
        LoginPage loginPage = new LoginPage(driver);
        List<Map<String, String>> loginRows = CsvDataReader.readCsvAsMaps("src/test/resources/testdata/LoginData.csv");
        Map<String, String> creds = loginRows.get(0);
        
        loginPage.open();
        loginPage.login(creds.get("Email"), creds.get("Password"));
        
        Thread.sleep(5000); // Wait for dashboard to load
        
        driver.get("https://app.getcalley.com/BulkUploadCsv.aspx");
        Thread.sleep(3000);

        org.openqa.selenium.WebElement listName = driver.findElement(org.openqa.selenium.By.cssSelector("[id$='txtlistname']"));
        listName.sendKeys("Debug Test List " + System.currentTimeMillis());

        org.openqa.selenium.WebElement agentSelect = driver.findElement(org.openqa.selenium.By.cssSelector("[id$='ddlagents']"));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].style.display = 'block';", agentSelect);
        new org.openqa.selenium.support.ui.Select(agentSelect).selectByIndex(1);

        org.openqa.selenium.WebElement fileInput = driver.findElement(org.openqa.selenium.By.cssSelector("input[type='file']"));
        java.io.File csv = new java.io.File("src/test/resources/testdata/Sample_File_in_.csv");
        fileInput.sendKeys(csv.getAbsolutePath());

        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "var dd = document.querySelector('[id$=\"ddlagents\"]');" +
            "var hd = document.querySelector('[id$=\"hdn_agents\"]');" +
            "if (dd && hd) { " +
            "  var opts = Array.from(dd.selectedOptions).map(function(o){return o.value;});" +
            "  hd.value = opts.join(',');" +
            "}" +
            "var btn = document.querySelector('[id$=\"btnUpload\"]');" +
            "if(btn) { btn.click(); }"
        );

        Thread.sleep(10000); // Wait for postback and mapping screen
        
        new org.openqa.selenium.support.ui.Select(driver.findElement(org.openqa.selenium.By.id("ddlbelongto_1"))).selectByVisibleText("FirstName");
        new org.openqa.selenium.support.ui.Select(driver.findElement(org.openqa.selenium.By.id("ddlbelongto_2"))).selectByVisibleText("Phone");
        new org.openqa.selenium.support.ui.Select(driver.findElement(org.openqa.selenium.By.id("ddlbelongto_3"))).selectByVisibleText("Notes");

        driver.findElement(org.openqa.selenium.By.cssSelector("[id$='btnUpload'][value='Import Data']")).click();
        Thread.sleep(10000); // Wait for final result

        String source = driver.getPageSource();
        try (PrintWriter out = new PrintWriter(new FileWriter("mapping_source.html"))) {
            out.println(source);
        }
    }
}
