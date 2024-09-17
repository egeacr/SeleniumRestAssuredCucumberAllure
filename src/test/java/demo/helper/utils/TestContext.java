package demo.helper.utils;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class TestContext {

    @Getter
    @Setter
    public WebDriver driver;
    @Getter
    private GlobalData globalData;

    public TestContext() {
        globalData = new GlobalData();
    }

    public void clearBrowserStorage() {
        ((JavascriptExecutor) driver).executeScript("window.sessionStorage.clear();");
        ((JavascriptExecutor) driver).executeScript("window.localStorage.clear();");
    }
}

