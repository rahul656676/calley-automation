package com.calley.automation.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Wraps ExtentReports so tests can log steps/screenshots and get a single
 * shared HTML report at test-output/ExtentReports/CalleyAutomationReport.html
 */
public class ExtentReportManager {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            String reportDir = ConfigReader.get("report.dir", "test-output/ExtentReports");
            String reportPath = reportDir + "/CalleyAutomationReport.html";

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setDocumentTitle("Calley Team Account - Automation Report");
            spark.config().setReportName("Registration | Login | Add Agent | Power Import (CSV)");

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Execution Date", new SimpleDateFormat("dd-MMM-yyyy HH:mm").format(new Date()));
            extent.setSystemInfo("Framework", "Selenium + TestNG (Page Object Model)");
        }
        return extent;
    }

    public static void createTest(String testName, String description) {
        ExtentTest extentTest = getInstance().createTest(testName, description);
        test.set(extentTest);
    }

    public static ExtentTest getTest() {
        return test.get();
    }

    public static void flush() {
        if (extent != null) {
            extent.flush();
        }
    }
}
