import demo.helper.factory.LoggerFactory;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.TestNGCucumberRunner;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;

import java.time.Duration;
import java.time.Instant;

@CucumberOptions(
        plugin = {"json:target/cucumber.json",
                "html:target/default-html-reports",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
                "rerun:failedScenarios.txt"
        },
        features = "src/test/resources/features",
        dryRun = false,
        tags = "@regression"
)
public class TestRunner extends AbstractTestNGCucumberTests {

    private static final Logger logger = LoggerFactory.getLogger(TestRunner.class);
    private TestNGCucumberRunner testNGCucumberRunner;
    private Instant startTime;
    private Instant endTime;

    @BeforeClass
    public void setup(ITestContext context) {
        startTime = Instant.now();
        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
        Object[][] scenarios = testNGCucumberRunner.provideScenarios();

    }

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return testNGCucumberRunner.provideScenarios();
    }

    @AfterSuite
    public void tearDown() {
        endTime = Instant.now();
        if (testNGCucumberRunner != null) {
            testNGCucumberRunner.finish();
        }
        logDuration();
    }

    private void logDuration() {
        Duration duration = Duration.between(startTime, endTime);
        long minutes = duration.toMinutes();
        long seconds = duration.minusMinutes(minutes).getSeconds();
        logger.info("*");
        logger.info("*");
        logger.info("*");
        logger.info("*");
        logger.info("*");
        logger.info("*");
        logger.info("*");
        logger.info("=====================================");
        logger.info("Test suite finished in: " + minutes + " minutes and " + seconds + " seconds.");
        logger.info("=====================================");
    }
}
