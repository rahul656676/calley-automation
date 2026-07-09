package com.calley.automation.utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtils {

    private ScreenshotUtils() {
    }

    /**
     * Captures a screenshot and stores it under the configured screenshot
     * directory, returning the absolute path so it can be attached to the
     * ExtentReports report on test failure.
     */
    public static String capture(WebDriver driver, String testName) {
        try {
            String dir = ConfigReader.get("screenshot.dir", "test-output/screenshots");
            File destDir = new File(dir);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = testName + "_" + timestamp + ".png";
            File destFile = new File(destDir, fileName);

            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(srcFile, destFile);
            return destFile.getAbsolutePath();
        } catch (IOException e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }
}
