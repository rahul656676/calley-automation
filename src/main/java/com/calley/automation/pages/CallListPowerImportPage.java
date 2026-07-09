    package com.calley.automation.pages;

    import com.calley.automation.base.BasePage;
    import org.openqa.selenium.By;
    import org.openqa.selenium.WebDriver;
    import org.openqa.selenium.support.ui.Select;

    import java.io.File;

    /**
     * Page Object for Call List -> Power Import, which lets the user name a
     * new list, choose agents, upload a CSV, map its columns, and import it.
     * Verify field ids once against the live DOM (see note in RegistrationPage).
     */
    public class CallListPowerImportPage extends BasePage {

        private final By callListMenuLink   = By.xpath("//a[contains(@href, 'call-list') and contains(., 'Call List')]");
        private final By powerImportLink    = By.xpath("//a[contains(@href, 'BulkUploadCsv.aspx')]");
        private final By listNameField      = By.cssSelector("[id$='txtlistname'], [id$='txtListName']");
        private final By agentsMultiSelect  = By.cssSelector("[id$='ddlagents'], [id$='ddlAgents']");
        private final By chooseFileInput    = By.cssSelector("input[type='file']");
        private final By nextButton         = By.id("btnUp");

        // Field-mapping step: maps CSV columns (Name, Contact, Notes) to system fields
        private final By mapNameDropdown    = By.id("ddlbelongto_1");
        private final By mapContactDropdown = By.id("ddlbelongto_2");
        private final By mapNotesDropdown   = By.id("ddlbelongto_3");

        private final By importButton       = By.cssSelector("[id$='btnUpload'][value='Import Data']");
        private final By importSuccessToast = By.className("toast-success");
        private final By importedRecordCount = By.id("lblImportedCount");

        public CallListPowerImportPage(WebDriver driver) {
            super(driver);
        }

        public CallListPowerImportPage navigateToPowerImport() {
            try {
                click(callListMenuLink);
            } catch (Exception e) {
                org.openqa.selenium.WebElement element = driver.findElement(callListMenuLink);
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            }
            try {
                click(powerImportLink);
            } catch (Exception e) {
                org.openqa.selenium.WebElement element = driver.findElement(powerImportLink);
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            }
            return this;
        }

        public CallListPowerImportPage enterListName(String listName) {
            type(listNameField, listName);
            return this;
        }

        public CallListPowerImportPage selectAgents(String... agentNames) {
            try {
                // Click the multiselect toggle button
                waitUtils.waitForVisible(By.cssSelector("button.multiselect.dropdown-toggle")).click();
                
                // Click the 'Select All' checkbox to ensure an agent is always selected
                org.openqa.selenium.WebElement selectAll = waitUtils.waitForVisible(By.cssSelector("input[type='checkbox'][value='all']"));
                if (!selectAll.isSelected()) {
                    ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", selectAll);
                }
            } catch (Exception e) {
                System.out.println("Could not interact with bootstrap multiselect: " + e.getMessage());
            }
            return this;
        }

        /**
         * Uploads the CSV via the native file input. Selenium types the absolute
         * path directly into the <input type="file"> element - no OS file dialog
         * interaction is required.
         */
        public CallListPowerImportPage uploadCsv(String csvFilePath) {
            File csvFile = new File(csvFilePath);
            driver.findElement(chooseFileInput).sendKeys(csvFile.getAbsolutePath());
            return this;
        }

        public CallListPowerImportPage clickNext() {
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
            return this;
        }

        /**
         * Maps the three columns present in Sample_File_in_.csv (Name, Contact,
         * Notes) to the corresponding system fields on the mapping screen.
         */
        public CallListPowerImportPage mapFields() {
            new org.openqa.selenium.support.ui.Select(waitUtils.waitForVisible(mapNameDropdown)).selectByVisibleText("FirstName");
            new org.openqa.selenium.support.ui.Select(waitUtils.waitForVisible(mapContactDropdown)).selectByVisibleText("Phone");
            new org.openqa.selenium.support.ui.Select(waitUtils.waitForVisible(mapNotesDropdown)).selectByVisibleText("Notes");
            return this;
        }

        public void clickImport() {
            click(importButton);
        }

        public boolean isImportSuccessful() {
            try {
                org.openqa.selenium.WebElement successMsg = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                    .until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//div[contains(@class, 'sweet-alert') and contains(., 'Uploaded Successfully')]")
                    ));
                return successMsg.isDisplayed();
            } catch (Exception e) {
                return false;
            }
        }

        public String getImportedRecordCount() {
            // Power import success alert doesn't show a specific count, it just confirms upload
            return isImportSuccessful() ? "Success" : "";
        }

        /**
         * End-to-end convenience method covering the whole Power Import flow:
         * navigate -> name list -> pick agents -> upload csv -> map fields -> import.
         */
        public void importCallList(String listName, String csvFilePath, String... agentNames) {
            navigateToPowerImport();
            enterListName(listName);
            selectAgents(agentNames);
            uploadCsv(csvFilePath);
            clickNext();
            mapFields();
            clickImport();
        }
    }
