package demo.hooks;

import demo.helper.ConfigurationReader;
import demo.helper.factory.DriverFactory;
import demo.helper.factory.LoggerFactory;
import demo.helper.utils.TestContext;
import io.cucumber.java.*;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.nio.file.Paths;
import java.util.HashMap;

public class Hooks {

    private static final Logger logger = LoggerFactory.getLogger(Hooks.class);
    private final TestContext context;
    private WebDriver driver;
    public static HashMap<String, String> failedScenarioNames;
    public static HashMap<String, String> successScenarioNames;
    private static final String DOWNLOADS_PATH = Paths.get(System.getProperty("user.dir"),
            "downloads").toString();

    public Hooks(TestContext context) {
        this.context = context;
    }

    @Before(order = 0)
    public void before(Scenario scenario) {
        System.setProperty("testName", scenario.getName().replaceAll("[^a-zA-Z0-9]", "_"));
        logger.info("BEFORE: THREAD ID : " + Thread.currentThread().getId() + "," +
                "SCENARIO NAME: " + scenario.getName());
        driver = DriverFactory.initializeDriver(context);
        context.setDriver(driver);
    }

    @After
    public void after(Scenario scenario) {
        logger.info("AFTER: THREAD ID : " + Thread.currentThread().getId() + "," +
                "SCENARIO NAME: " + scenario.getName());
        if (scenario.getStatus().name().equals("PASSED")) {
            logSuccessScenario(scenario);
        } else if (scenario.isFailed()) {
            logFailedScenario(scenario);
            captureScreenshot(scenario);
        } else {
            logger.info(scenario.getStatus().name() + " -> SCENARIO NAME: " + scenario.getName());
        }
        if (driver != null) {
            driver.quit();
        }
    }


    private void captureScreenshot(Scenario scenario) {
        final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        scenario.attach(screenshot, "image/png", "screenshot");
    }

    private void logFailedScenario(Scenario scenario) {
        logger.info("FAILED!!! -> SCENARIO NAME: " + scenario.getName());
    }

    private void logSuccessScenario(Scenario scenario) {
        logger.info("PASSED!!! -> SCENARIO NAME: " + scenario.getName());
    }





}
