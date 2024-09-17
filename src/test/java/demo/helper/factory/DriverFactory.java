package demo.helper.factory;

import demo.helper.ConfigurationReader;
import demo.helper.utils.TestContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.HashMap;
import java.util.Map;

public class DriverFactory {

    public static WebDriver initializeDriver(TestContext context) {

        WebDriver driver;
        String browser = System.getProperty("browser", ConfigurationReader.get("browser"));

        switch (browser) {
            case "chrome" -> {
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments(
                        "--ignore-certificate-errors",
                        "--allow-insecure-localhost",
                        "--acceptInsecureCerts",
                        "disable-infobars",
                        "--disable-dev-shm-usage",
                        "--ignore-ssl-errors=yes",
                        "--ignore-certificate-errors",
                        "--remote-allow-origins=*",
                        "--safebrowsing-disable-download-protection",
                        "safebrowsing-disable-extension-blacklist",
                        "--window-size=" + ConfigurationReader.get("windowSize"));
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("download.prompt_for_download", false);
                prefs.put("download.directory_upgrade", true);
                prefs.put("safebrowsing_enabled", true);
                prefs.put("download.extensions_to_open", "pdf");
                prefs.put("profile.default_content_settings.popups", 0);
                prefs.put("profile.content_settings.exceptions.automatic_downloads.*.setting", 1);

                chromeOptions.setExperimentalOption("prefs",
                        prefs);
                //LOGGER.info("***** The Browser is Chrome *****");
                driver = new ChromeDriver(chromeOptions);
            }
            case "edge" -> {
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments(
                        "--incognito",
                        "--ignore-certificate-errors",
                        "--allow-insecure-localhost",
                        "--acceptInsecureCerts",
                        "disable-infobars",
                        "--disable-dev-shm-usage",
                        "--ignore-ssl-errors=yes",
                        "--ignore-certificate-errors",
                        "--remote-allow-origins=*",
                        "--window-size=" + ConfigurationReader.get("windowSize"));

                driver = new EdgeDriver(edgeOptions);
            }
            case "firefox" -> {
                                driver = new FirefoxDriver();
            }
            case "chrome-headless" -> {
                ChromeOptions chromeHeadlessOptions = new ChromeOptions();
                chromeHeadlessOptions.addArguments(
                        "--ignore-certificate-errors",
                        "--allow-insecure-localhost",
                        "--acceptInsecureCerts",
                        "disable-infobars",
                        "--disable-dev-shm-usage",
                        "--ignore-ssl-errors=yes",
                        "--ignore-certificate-errors",
                        "--remote-allow-origins=*",
                        "--headless",
                        "--safebrowsing-disable-download-protection",
                        "safebrowsing-disable-extension-blacklist",
                        "--window-size=" + ConfigurationReader.get("windowSize"),
                        "--headless");

                Map<String, Object> prefs = new HashMap<>();
                prefs.put("download.prompt_for_download", false);
                prefs.put("download.directory_upgrade", true);
                prefs.put("safebrowsing_enabled", true);
                prefs.put("download.extensions_to_open", "pdf");
                prefs.put("profile.default_content_settings.popups", 0);
                prefs.put("profile.content_settings.exceptions.automatic_downloads.*.setting", 1);
                chromeHeadlessOptions.setExperimentalOption("prefs",
                        prefs);
                driver = new ChromeDriver(chromeHeadlessOptions);
            }
            case "edge-headless" -> {
                EdgeOptions edgeHeadlessOptions = new EdgeOptions();
                edgeHeadlessOptions.addArguments(
                        "--incognito",
                        "--ignore-certificate-errors",
                        "--no-sandbox",
                        "--allow-insecure-localhost",
                        "--acceptInsecureCerts",
                        "disable-infobars",
                        "--disable-dev-shm-usage",
                        "--ignore-ssl-errors=yes",
                        "--ignore-certificate-errors",
                        "--remote-allow-origins=*",
                        "--headless",
                        "--window-size=" + ConfigurationReader.get("windowSize"),
                        "--headless");

                driver = new EdgeDriver(edgeHeadlessOptions);
            }
            default -> throw new IllegalStateException("INVALID BROWSER: " + browser);
        }
        driver.manage().window().maximize();
        return driver;
    }

}
