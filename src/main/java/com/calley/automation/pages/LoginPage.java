package com.calley.automation.pages;

import com.calley.automation.base.BasePage;
import com.calley.automation.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private final By emailField = By.id("txtEmailId");
    private final By passwordField = By.id("txtPassword");
    private final By loginButton = By.id("btnLogIn");

    private final By errorMessage =
            By.xpath("//*[contains(@class,'validation') or contains(@class,'error')]");

    // Dashboard me "Hi Rahul" / "Hi <Name>" text aata hai
    private final By dashboardMarker =
            By.xpath("//*[contains(text(),'Hi')]");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        navigateTo(ConfigReader.loginUrl());
        return this;
    }

    public LoginPage enterEmail(String email) {
        type(emailField, email);
        return this;
    }

    public LoginPage enterPassword(String password) {
        type(passwordField, password);
        return this;
    }

    public void clickLogin() {
        click(loginButton);
    }

    public void login(String email, String password) {
        open();
        enterEmail(email);
        enterPassword(password);
        clickLogin();
    }

    public boolean isLoginSuccessful() {

        String url = driver.getCurrentUrl().toLowerCase();

        return url.contains("dashboard")
                || url.contains("home")
                || isDisplayed(dashboardMarker);
    }

    public String getErrorMessage() {
        return isDisplayed(errorMessage) ? getText(errorMessage) : "";
    }
}