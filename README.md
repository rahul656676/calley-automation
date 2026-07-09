# Calley Team Account - Automation Testing (Selenium + TestNG + Java)

## Project Structure
```
calley-automation/
├── pom.xml
├── testng.xml
├── src
│   ├── main/java/com/calley/automation
│   │   ├── base/          -> DriverManager, BasePage
│   │   ├── pages/          -> RegistrationPage, LoginPage, AgentPage, CallListPowerImportPage
│   │   └── utils/          -> ConfigReader, CsvDataReader, WaitUtils, ScreenshotUtils, ExtentReportManager
│   ├── main/resources/log4j2.xml
│   └── test/java/com/calley/automation/tests
│       ├── BaseTest.java
│       ├── RegistrationTest.java
│       ├── LoginTest.java
│       ├── AddAgentTest.java
│       └── PowerImportCsvUploadTest.java
│   └── test/resources
│       ├── config.properties
│       └── testdata/
│           ├── UserData.csv
│           ├── LoginData.csv
│           ├── AgentData.csv
│           └── Sample_File_in_.csv
```

## Prerequisites
- Java JDK 11+
- Maven 3.6+
- Google Chrome installed (WebDriverManager auto-downloads the matching driver)

## Setup
1. Import the project into Eclipse/IntelliJ as an existing Maven project.
2. Run `mvn clean install -DskipTests` once to pull all dependencies.

## Configuration
Edit `src/test/resources/config.properties` to change:
- `browser` (chrome/firefox/edge), `headless` (true/false)
- `registration.url`, `login.url`
- test data file paths
- `plan.name` (default: `Calley Teams`)

Update `src/test/resources/testdata/UserData.csv`, `LoginData.csv`, and
`AgentData.csv` with real values before running against the live site.

## Running the tests
```
mvn clean test
```
This runs `testng.xml` in order: Registration -> Login -> Add Agent -> Power Import CSV Upload.

Run a single class:
```
mvn -Dtest=LoginTest test
```

## Reports & Screenshots
- HTML report: `test-output/ExtentReports/CalleyAutomationReport.html`
- Failure screenshots: `test-output/screenshots/`
- Logs: `test-output/logs/automation.log`

## Notes on Locators
Locators in the Page Object classes (`RegistrationPage`, `LoginPage`,
`AgentPage`, `CallListPowerImportPage`) are written against typical field
ids/names for this type of form. Since the live DOM can only be confirmed
from an active browser session, open each page, right-click -> Inspect, and
adjust any `By.id(...)` / `By.xpath(...)` values that differ from the actual
markup. No other framework code needs to change - flows, data-driven tests,
and reporting are already wired to the Page Object methods.

## Test Scenarios Covered
1. **User Registration** - `RegistrationTest` (data-driven from `UserData.csv`, selects "Calley Teams" plan)
2. **User Login** - `LoginTest` (data-driven from `LoginData.csv`)
3. **Add Agents** - `AddAgentTest` (logs in, then adds agent from `AgentData.csv`)
4. **Upload CSV of List (Power Import)** - `PowerImportCsvUploadTest` (logs in, navigates to Call List > Power Import, uploads `Sample_File_in_.csv`, maps Name/Contact/Notes fields, imports)
