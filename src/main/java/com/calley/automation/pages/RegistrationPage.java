package com.calley.automation.pages;

import com.calley.automation.base.BasePage;
import com.calley.automation.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Map;

public class RegistrationPage extends BasePage {

    // ===========================
    // Locators
    // ===========================

    private final By nameField = By.id("txtName");
    private final By emailField = By.id("txtEmail");
    private final By passwordField = By.id("txtPassword");
    private final By phoneField = By.id("txt_mobile");

    private final By termsCheckbox = By.id("checkbox-signup");
    private final By submitButton = By.id("btnSignUp");

    private final By successBanner =
            By.xpath("//*[contains(text(),'Success') or contains(text(),'success')]");

    private final By errorBanner =
            By.xpath("//*[contains(@class,'error') or contains(@class,'validation')]");

    public RegistrationPage(WebDriver driver) {
        super(driver);
    }

    public RegistrationPage open() {
        navigateTo(ConfigReader.registrationUrl());
        return this;
    }

    public RegistrationPage enterName(String value) {
        type(nameField, value);
        return this;
    }

    public RegistrationPage enterEmail(String value) {
        type(emailField, value);
        return this;
    }

    public RegistrationPage enterPassword(String value) {
        type(passwordField, value);
        return this;
    }

    public RegistrationPage enterPhone(String value) {
        type(phoneField, value);
        return this;
    }

    public RegistrationPage acceptTerms() {

        org.openqa.selenium.WebElement cb = driver.findElement(termsCheckbox);
        if (!cb.isSelected()) {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", cb);
        }

        return this;
    }

    public void submitForm() {
        click(submitButton);
    }

    public boolean isRegistrationSuccessful() {
        return isDisplayed(successBanner);
    }

    public String getErrorMessage() {
        return isDisplayed(errorBanner) ? getText(errorBanner) : "";
    }

    public void registerUser(Map<String, String> userData) {

        open();

        enterName(userData.get("FirstName"));

        enterEmail(userData.get("Email"));

        enterPassword(userData.get("Password"));

        // India (+91) is already selected by default.
        // No need to select country.

        enterPhone(userData.get("Phone"));

        // Industry dropdown is no longer a normal <select>.
        // Skipped for current UI.

        acceptTerms();

        submitForm();
    }
}