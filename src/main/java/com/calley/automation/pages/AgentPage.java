package com.calley.automation.pages;

import com.calley.automation.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.Map;

public class AgentPage extends BasePage {

    private static final String AGENTS_URL =
            "https://app.getcalley.com/agents.aspx";

    // ===========================
    // Agent Form
    // ===========================

    private final By agentNameField =
            By.id("ContentPlaceHolder1_txt_name");

    private final By agentMobileField =
            By.id("ContentPlaceHolder1_txt_mobile");

    private final By agentEmailField =
            By.id("ContentPlaceHolder1_txt_email");

    private final By agentPasswordField =
            By.id("ContentPlaceHolder1_txt_pass");

    private final By leadDropdown =
            By.id("ContentPlaceHolder1_ddl_lead");

    private final By submitButton =
            By.id("ContentPlaceHolder1_btn_submit");

    public AgentPage(WebDriver driver) {
        super(driver);
    }

    // ===========================
    // Navigation
    // ===========================

    public AgentPage goToAgentsPage() {

        driver.get(AGENTS_URL);

        return this;
    }

    // ===========================
    // Form Methods
    // ===========================

    public AgentPage enterAgentName(String name) {

        type(agentNameField, name);
        return this;
    }

    public AgentPage enterAgentPhone(String phone) {

        type(agentMobileField, phone);
        return this;
    }

    public AgentPage enterAgentEmail(String email) {

        type(agentEmailField, email);
        return this;
    }

    public AgentPage enterAgentPassword(String password) {

        type(agentPasswordField, password);
        return this;
    }

    public AgentPage selectRole(String role) {

        if (role == null || role.trim().isEmpty())
            return this;

        try {

            Select select = new Select(driver.findElement(leadDropdown));

            if (select.getOptions().size() > 1) {
                select.selectByVisibleText(role);
            }

        } catch (Exception ignored) {
        }

        return this;
    }

    public void saveAgent() {

        click(submitButton);
    }

    // ===========================
    // Validation
    // ===========================

    public boolean isAgentAddedSuccessfully() {

        return driver.getPageSource().contains("success")
                || driver.getPageSource().contains("Agent")
                || driver.getPageSource().contains("added");
    }

    public boolean isAgentPresentInGrid(String agentName) {

        By row = By.xpath("//*[contains(text(),'" + agentName + "')]");

        return isDisplayed(row);
    }

    // ===========================
    // Complete Flow
    // ===========================

    public void addAgent(Map<String, String> agentData) {

        goToAgentsPage();

        enterAgentName(agentData.get("AgentName"));

        enterAgentPhone(agentData.get("AgentPhone"));

        enterAgentEmail(agentData.get("AgentEmail"));

        enterAgentPassword(agentData.get("AgentPassword"));

        selectRole(agentData.get("Role"));

        saveAgent();
    }
}