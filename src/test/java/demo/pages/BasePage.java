package demo.pages;

import demo.helper.factory.LoggerFactory;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;


import java.time.Duration;
import java.util.*;

public class BasePage {

    private static final Logger logger = LoggerFactory.getLogger(BasePage.class);
    private final WebDriver driver;
    private final int WAIT_TIME = 10000;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver,this);
    }

    private JavascriptExecutor getExecutor() {
        return (JavascriptExecutor) driver;
    }
    private WebDriverWait getWait() {
        return new WebDriverWait(driver, Duration.ofMillis(WAIT_TIME));
    }
    private WebDriverWait getWait(int milliseconds) {
        return new WebDriverWait(driver, Duration.ofMillis(milliseconds));
    }


    ///**************************************************** CLICK METHODS **************************************************
    //************************************************************************************************************************
    public void click(WebElement element) {
        try {
            element.click();
        } catch (StaleElementReferenceException | NoSuchElementException |
                 ElementNotInteractableException e) {
            try {
                waitForVisibilityOfElement(element);
                element.click();
            } catch (StaleElementReferenceException | NoSuchElementException |
                     ElementNotInteractableException f) {
                jsClick(element);
            }
        }
    }

    public void click(WebElement element, int milliseconds) {
        try {
            element.click();
        } catch (StaleElementReferenceException | NoSuchElementException |
                 ElementNotInteractableException e) {
            try {
                waitForVisibilityOfElement(element, milliseconds);
                element.click();
            } catch (StaleElementReferenceException | NoSuchElementException |
                     ElementNotInteractableException f) {
                jsClick(element);
            }
        }
    }

    public void clickAfterWait(WebElement element) {
        sleep(3000);
        click(element);
    }

    public void clickAndWait(WebElement element) {
        click(element);
        sleep(3000);
    }

    public void jsClick(WebElement element) {
        waitUntilPageLoaded();
        getExecutor().executeScript("arguments[0].click();", element);
    }


    public void clickText(String elementText) {
        WebElement element = findElement(
                By.xpath("(//*[normalize-space()='" + elementText + "'])[1]"));
        click(element);
    }

    public void retryClick(WebElement clickElement, WebElement resultElement) {
        int maxRetries = 5;
        int retries = 0;

        while (retries < maxRetries) {
            try {
                waitForVisibilityOfElement(clickElement);
                clickElement.click();
                try {
                    getWait(3000).until(ExpectedConditions.visibilityOf(resultElement));
                    return;
                } catch (TimeoutException e) {
                    // No logging here, just retry
                }
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                // No logging here, just retry
            }
            retries++;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void retryClickUntilElementDisappear(WebElement element, WebElement disseparedElement) {
        int attempts = 0;
        boolean isDisappeared = false;

        while (attempts < 5 && !isDisappeared) {
            try {
                element.click();
                Thread.sleep(2000);
                isDisappeared = !disseparedElement.isDisplayed();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (NoSuchElementException e) {
                isDisappeared = true;
            }
            attempts++;
        }
    }

    //************************************************** WRITE METHODS *****************************************************
    //************************************************************************************************************************
    public void writeText(WebElement element, String text) {
        if (text != null) {
            try {
                element.clear();
                element.sendKeys(text);
            } catch (Exception e) {
                waitForVisibilityOfElement(element);
                element.clear();
                element.sendKeys(text);
            }
        }
    }

    public void writeText(WebElement element, String text, int milliseconds) {
        if (text != null) {
            try {
                element.clear();
                element.sendKeys(text);
            } catch (Exception e) {
                waitForVisibilityOfElement(element, milliseconds);
                element.clear();
                element.sendKeys(text);
            }
        }
    }

    public void writeTextAfterWait(WebElement element, String text) {
        sleep(2000);
        writeText(element, text);
    }

    public void writeTextAndWait(WebElement element, String text) {
        writeText(element, text);
        sleep(3000);
    }

    public void jsWriteText(WebElement element, String text) {
        getExecutor().executeScript("arguments[0].value = arguments[1];", element, text);
    }

    public void pressEnter(WebElement element) {
        element.sendKeys(Keys.ENTER);
    }

    public void writeTextOneByOne(WebElement element, String inputString) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < inputString.length(); i++) {
            char c = inputString.charAt(i);
            sb.append(c);
        }
        waitForVisibilityOfElement(element);
        element.sendKeys(sb);
    }

    public void writeTextWithBackSpaceClear(WebElement element, String text) {
        if (text != null) {
            try {
                clearWithBackSpaceShortly(element);
                element.sendKeys(text);
            } catch (Exception e) {
                waitForVisibilityOfElement(element);
                clearWithBackSpaceShortly(element);
                element.sendKeys(text);
            }
        }
    }

    public void writeTextWithEnter(WebElement element, String text) {
        if (text != null) {
            try {
                element.sendKeys(text + Keys.ENTER);
            } catch (Exception e) {
                waitForVisibilityOfElement(element);
                element.sendKeys(text + Keys.ENTER);
            }
        }
    }

    public void writeTextWithPressDownAndEnter(WebElement element, String text) {
        if (text != null) {
            try {
                clearWithBackSpaceShortly(element);
                element.sendKeys(text);
                sleep(3500);
                element.sendKeys(Keys.DOWN, Keys.ENTER);
            } catch (NoSuchElementException | TimeoutException | StaleElementReferenceException e) {
                waitForVisibilityOfElement(element);
                clearWithBackSpaceShortly(element);
                element.sendKeys(text);
                sleep(3500);
                element.sendKeys(Keys.DOWN, Keys.ENTER);
            }
        }
    }

    public void clearInputText(WebElement element) {
        try {
            element.clear();
        } catch (TimeoutException | NoSuchElementException | StaleElementReferenceException a) {
            waitForVisibilityOfElement(element);
            element.clear();
        }
    }

    public void clearInputWithBackSpace(WebElement element) {
        var counter = 0;
        try {
            while (!element.getAttribute("value").isEmpty() && counter < 300) {
                element.sendKeys(Keys.BACK_SPACE);
                counter++;
            }
        } catch (TimeoutException | NoSuchElementException | StaleElementReferenceException a) {
            waitForVisibilityOfElement(element);
            while (!element.getAttribute("value").isEmpty() && counter < 300) {
                element.sendKeys(Keys.BACK_SPACE);
                counter++;
            }
        }
    }

    public void clearWithBackSpaceShortly(WebElement element) {
        var counter = 0;
        try {
            while (!element.getAttribute("value").isEmpty() && counter < 50) {
                element.sendKeys(Keys.BACK_SPACE);
                counter++;
            }
        } catch (TimeoutException | NoSuchElementException | StaleElementReferenceException a) {
            waitForVisibilityOfElement(element);
            while (!element.getAttribute("value").isEmpty() && counter < 50) {
                element.sendKeys(Keys.BACK_SPACE);
                counter++;

            }
        }
    }


    //************************************************** SELECT METHODS *****************************************************
//************************************************************************************************************************
    public void selectComboBoxOption(WebElement dropdownElement, WebElement dropdownInput,
                                     String text) {
        if (text != null) {
            try {
                waitForVisibilityOfElement(dropdownElement);
                retryClick(dropdownElement, dropdownInput);
                sleep(1000);
                clearInputText(dropdownInput);
                dropdownInput.sendKeys(text);
                sleep(1500);
                dropdownInput.sendKeys(Keys.DOWN, Keys.ENTER);
            } catch (StaleElementReferenceException | NoSuchElementException | TimeoutException |
                     ElementNotInteractableException f) {
                scrollToElement(dropdownElement);
                retryClick(dropdownElement, dropdownInput);
                sleep(1000);
                clearInputText(dropdownInput);
                dropdownInput.sendKeys(text);
                sleep(1000);
                dropdownInput.sendKeys(Keys.DOWN, Keys.ENTER);
            }
        }
    }

    public void selectComboBoxOption(WebElement dropdownElement, WebElement dropdownInput,
                                     String text, int milliseconds) {
        if (text != null) {
            try {
                waitForVisibilityOfElement(dropdownElement);
                retryClick(dropdownElement, dropdownInput);
                sleep(1000);
                clearInputText(dropdownInput);
                dropdownInput.sendKeys(text);
                sleep(milliseconds);
                dropdownInput.sendKeys(Keys.DOWN, Keys.ENTER);
            } catch (StaleElementReferenceException | NoSuchElementException | TimeoutException |
                     ElementNotInteractableException f) {
                scrollToElement(dropdownElement);
                retryClick(dropdownElement, dropdownInput);
                sleep(1000);
                clearInputText(dropdownInput);
                dropdownInput.sendKeys(text);
                sleep(1000);
                dropdownInput.sendKeys(Keys.DOWN, Keys.ENTER);
            }
        }
    }

    public void selectDropdownByClicking(WebElement dropdown, String dropdownOptionText) {
        click(dropdown);
        WebElement dropdownOption = null;
        try {
            dropdownOption = findElement(
                    By.xpath(""));
        }catch (Exception a){
            click(dropdown);
            dropdownOption = findElement(
                    By.xpath(""));
        }
        click(dropdownOption);
    }


    public List<String> getDropdownOptionsIntoList(WebElement dropdown) {
        scrollToElement(dropdown);
        sleep(3000);
        click(dropdown);
        waitForVisibilityOfElement(By.xpath("//p-dropdownitem/li"));
        sleep(5000);
        List<WebElement> dropdownElements = driver.findElements(By.xpath("//p-dropdownitem/li"));
        List<String> optionsText = new ArrayList<>();
        for (WebElement element : dropdownElements) {
            optionsText.add(getText(element));
        }
        return optionsText;
    }



    //************************************************** SCROLL METHODS *****************************************************
//************************************************************************************************************************
    public void scrollToElement(WebElement element) {
        try {
            getExecutor().executeScript("arguments[0].scrollIntoView(true);", element);
        } catch (NoSuchSessionException | NoSuchElementException | TimeoutException a) {
            waitForVisibilityOfElement(element);
            getExecutor().executeScript("arguments[0].scrollIntoView(true);", element);
        }
    }


    //************************************************** WAIT METHODS *****************************************************
//************************************************************************************************************************
    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }



    public void waitForVisibilityOfElement(WebElement element) {
        getWait().until(ExpectedConditions.visibilityOf(element));
    }

    public void waitForVisibilityOfElement(WebElement element, int milliseconds) {
        getWait(milliseconds).until(ExpectedConditions.visibilityOf(element));
    }

    public void waitForVisibilityOfElement(By locator) {
        getWait().ignoring(StaleElementReferenceException.class)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void waitForVisibilityOfAllListOfElements(List<WebElement> elements, int milliseconds) {
        getWait(milliseconds).ignoring(StaleElementReferenceException.class)
                .until(ExpectedConditions.visibilityOfAllElements(elements));

    }

    public WebElement findElement(By locator) {
        try {
            return getWait().ignoring(StaleElementReferenceException.class)
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (StaleElementReferenceException | NoSuchElementException | TimeoutException |
                 ElementNotInteractableException f
        ) {
            sleep(3000);
            return getWait().ignoring(StaleElementReferenceException.class)
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
        }
    }

    public List<WebElement> findElements(By locator) {
        try {
            return getWait().ignoring(StaleElementReferenceException.class)
                    .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
        } catch (StaleElementReferenceException | NoSuchElementException | TimeoutException |
                 ElementNotInteractableException f) {
            sleep(3000);
            return getWait().ignoring(StaleElementReferenceException.class)
                    .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
        }
    }

    public WebElement findElement(By locator, int milliseconds) {
        try {
            return getWait(milliseconds).ignoring(StaleElementReferenceException.class)
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (StaleElementReferenceException | NoSuchElementException | TimeoutException |
                 ElementNotInteractableException a) {
            return getWait(milliseconds).ignoring(StaleElementReferenceException.class)
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
        }
    }

    public void waitUntilPageLoaded() {
        getWait().until((WebDriver driver) ->
                ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete")
        );
    }

    public void waitForClickAbilityOfElement(WebElement element) {
        try {
            getWait().until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception e) {
        }
    }

    public void waitForClickAbilityOfElement(WebElement element, int milliseconds) {
        try {
            getWait(milliseconds).until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception e) {
        }
    }

    public void waitForInvisibilityOfElement(WebElement element, int milliseconds) {
        try {
            getWait(milliseconds).until(ExpectedConditions.invisibilityOf(element));
        } catch (Exception e) {
            throw new RuntimeException(
                    "Element is still visible after " + milliseconds + " milliseconds");
        }
    }

    public void waitForInvisibilityOfElement(WebElement element) {
        try {
            getWait().until(ExpectedConditions.invisibilityOf(element));
        } catch (Exception e) {
        }
    }

    public void waitUntilElementDisappear(WebElement element, int milliseconds) {
        try {
            new WebDriverWait(driver, Duration.ofMillis(milliseconds)).until(
                    ExpectedConditions.invisibilityOf(element));
        } catch (TimeoutException e) {
        }
    }


    public void waitText(String text) {
        WebElement element = findElement(
                By.xpath("(//*[contains(text(), '" + text + "')])[1]"));
        getWait().until(ExpectedConditions.visibilityOf(element));
    }

    public List<WebElement> waitElements(By by) {
        return getWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
    }

    //*********************************************** BOOLEAN METHODS **********************************************
//*********************************************************************************************************
    public boolean isElementVisible(WebElement element) {
        try {
            getWait().until(ExpectedConditions.visibilityOf(element));
            return element.isDisplayed();
        } catch (StaleElementReferenceException | NoSuchElementException | TimeoutException |
                 ElementNotInteractableException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isElementVisible(WebElement element, int milliseconds) {
        try {
            getWait(milliseconds).until(ExpectedConditions.visibilityOf(element));
            return element.isDisplayed();
        } catch (StaleElementReferenceException | NoSuchElementException | TimeoutException |
                 ElementNotInteractableException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isElementVisible(By locator, int milliseconds) {
        try {
            getWait(milliseconds).until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isTextVisible(String text) {
        try {
            String xpath = "(//*[contains(text(), \"" + text + "\")])[1]";
            WebElement element = findElement(By.xpath(xpath));
            return element.isDisplayed();
        } catch (StaleElementReferenceException | NoSuchElementException | TimeoutException |
                 ElementNotInteractableException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTextVisible(String text, int milliseconds) {
        try {
            String xpath = "(//*[contains(text(), \"" + text + "\")])[1]";
            WebElement element = findElement(By.xpath(xpath), milliseconds);
            return element.isDisplayed();
        } catch (StaleElementReferenceException | NoSuchElementException | TimeoutException |
                 ElementNotInteractableException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isElementAttContainsText(WebElement element, String attribute, String text) {
        try {
            waitForVisibilityOfElement(element);
            return element.getAttribute(attribute).contains(text);
        } catch (StaleElementReferenceException | NoSuchElementException | TimeoutException a) {
            sleep(2000);
            return element.getAttribute(attribute).contains(text);
        }
    }

    private boolean isElementAttNotContainsText(WebElement element, String attribute, String text) {
        try {
            waitForVisibilityOfElement(element);
            return !element.getAttribute(attribute).contains(text);
        } catch (StaleElementReferenceException | NoSuchElementException | TimeoutException a) {
            sleep(2000);
            return !element.getAttribute(attribute).contains(text);
        }
    }


    private boolean isElementHasAttribute(WebElement element, String attribute) {
        try {
            waitForVisibilityOfElement(element);
            return element.getAttribute(attribute) != null;
        } catch (StaleElementReferenceException | NoSuchElementException | TimeoutException a) {
            return element.getAttribute(attribute) != null;
        }
    }




    //********************************************* GET METHODS **********************************************************
//********************************************************************************************************************

    public String getText(WebElement element) {
        try {
            waitForVisibilityOfElement(element);
            return element.getText();
        } catch (Exception a) {
            sleep(1000);
            return element.getText();
        }

    }

    public String getText(WebElement element, int milliseconds) {
        try {
            waitForVisibilityOfElement(element, milliseconds);
            return element.getText();
        } catch (Exception a) {
            sleep(1000);
            return element.getText();
        }
    }

    public String getAttribute(WebElement element, String attribute) {
        try {
            waitForVisibilityOfElement(element);
            return element.getAttribute(attribute);
        } catch (StaleElementReferenceException | NoSuchElementException a) {
            sleep(1000);
            return element.getAttribute(attribute);
        }
    }

    public List<String> getListOfElementTexts(List<WebElement> elements){
        waitForVisibilityOfElement(elements.get(0));
        List<String> textList = new ArrayList<>();
        for(WebElement element: elements){
            textList.add(element.getText());
        }
        return textList;
    }


    //********************************************* ASSERT METHODS ********************************************************
//********************************************************************************************************************
    public void AssertEquals(Object actual, Object expected, String message) {
        Assert.assertEquals(actual, expected, message);
    }

    public void AssertEqualsGetText(WebElement textElement, String expected, String message) {
        waitForVisibilityOfElement(textElement);
        String actual = getText(textElement);
        Assert.assertEquals(actual, expected, message);
    }


    public void AssertTrue(boolean condition, String message) {
        Assert.assertTrue(condition, message);
    }


    public void AssertContains(String actual, String expected, String message) {
        try {
            Assert.assertTrue(actual.contains(expected), message);
        } catch (AssertionError e) {
            String failureMessage = String.format("%s%nActual: %s%nExpected: %s", message, actual,
                    expected);
            System.err.println(failureMessage);
            throw new AssertionError(failureMessage, e);
        }
    }

    public void AssertListsEqual(List<String> actualList, List<String> expectedList,
                                        String message) {
        Set<String> actualSet = new HashSet<>(actualList);
        Set<String> expectedSet = new HashSet<>(expectedList);
        Assert.assertEquals(actualSet, expectedSet, message);
    }



    public void AssertNotContains(String actual, String expected, String message) {
        try {
            Assert.assertFalse(actual.contains(expected), message);
        } catch (AssertionError e) {
            String failureMessage = String.format("%s%nActual: %s%nExpected to not contain: %s", message,
                    actual, expected);
            System.err.println(failureMessage);
            throw new AssertionError(failureMessage, e);
        }
    }


    public void AssertVisibilityOfElement(WebElement element, String message) {
        Assert.assertTrue(isElementVisible(element), message);
    }


    public void AssertInvisibilityOfElement(WebElement element, String message) {
        try {
            boolean isVisible = isElementVisible(element,100);
            Assert.assertFalse(isVisible, message);
        } catch (Exception e) {
            String failureMessage = String.format("%s%nElement should be invisible but was visible.",
                    message);
            System.err.println(failureMessage);
            throw new AssertionError(failureMessage, e);
        }
    }

    public void AssertInvisibilityOfText(String text, String message) {
        try {
            boolean isVisible = isTextVisible(text, 2000);
            Assert.assertFalse(isVisible, message);
        } catch (Exception e) {
            String failureMessage = String.format("%s%nText should be invisible but was visible: %s",
                    message, text);
            System.err.println(failureMessage);
            throw new AssertionError(failureMessage, e);
        }
    }



    public void AssertElementHasAttribute(WebElement element, String attribute,
                                                 String message) {
        try {
            boolean result = isElementHasAttribute(element, attribute);
            Assert.assertTrue(result, message);
        } catch (Exception e) {
            String failureMessage = String.format("%s%nElement should have attribute %s but did not.",
                    message, attribute);
            System.err.println(failureMessage);
            throw new AssertionError(failureMessage, e);
        }
    }




    //********************************************* TAB & FRAME METHODS ********************************************************
//********************************************************************************************************************
    public void waitForNewTab() {
        try {
            getWait().until(ExpectedConditions.numberOfWindowsToBe(2));
        } catch (TimeoutException e) {
            sleep(3000);
        }
        sleep(1500);
    }

    public void switchToLastTab() {
        List<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(tabs.size() - 1));
        sleep(2000);
    }

    public void switchToFirstTab() {
        List<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(0));
        sleep(2000);
    }

    public void switchToSecondTab() {
        List<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
    }

    public void closeLastTab() {
        switchToLastTab();
        driver.close();
        switchToFirstTab();
        List<String> tabs = new ArrayList<>(driver.getWindowHandles());
    }

    public int closeFirstTab() {
        switchToFirstTab();
        driver.close();
        List<String> tabs = new ArrayList<>(driver.getWindowHandles());
        return tabs.size();
    }

    public void openNewTab() {
        getExecutor().executeScript("window.open()");
    }

    public void openAndSwitchNewWindow(String expectedTitle) {
        String mainWindowHandle = driver.getWindowHandle();
        Set<String> windowHandles = driver.getWindowHandles();

        for (String handle : windowHandles) {
            if (!handle.equals(mainWindowHandle)) {
                driver.switchTo().window(handle);
                getWait().until(ExpectedConditions.titleContains(expectedTitle));
                return;
            }
        }
        throw new NoSuchWindowException(
                "No window found with title containing '" + expectedTitle + "'");
    }

    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }

    public void switchToFrame(String id) {
        driver.switchTo().frame(id);
    }

    public void switchToFrame(WebElement element) {
        waitForVisibilityOfElement(element);
        driver.switchTo().frame(element);
    }



    //************************************************** MIX METHODS ****************************************************
//************************************************************************************************************************
    public void navigateTo(String url, By elementLocator) {
        logger.info("Navigating to URL: " + url);
        driver.get(url);
        try {
            getWait().until(ExpectedConditions.visibilityOfElementLocated(elementLocator));
        } catch (TimeoutException e) {
            refreshPage();
            driver.get(url);
            try {
                getWait(10000).until(ExpectedConditions.visibilityOfElementLocated(elementLocator));
            } catch (TimeoutException ex) {
                logger.error("Element not visible after reattempting navigation. URL: " + url);
            }
        } catch (StaleElementReferenceException e) {
            logger.warn("Caught StaleElementReferenceException, retrying...");
            getWait(10000).until(ExpectedConditions.visibilityOfElementLocated(elementLocator));
        }
    }


    public void navigateTo(String url, WebElement element) {
        logger.info("Navigating to URL: " + url);
        driver.get(url);
        waitUntilPageLoaded();
        try {
           waitForVisibilityOfElement(element);
        } catch (TimeoutException e) {
            refreshPage();
            driver.get(url);
            try {
                waitForVisibilityOfElement(element);
            } catch (TimeoutException ex) {
                logger.error("Element not visible after reattempting navigation. URL: " + url);
            }
        } catch (StaleElementReferenceException e) {
            logger.warn("Caught StaleElementReferenceException, retrying...");
            waitForVisibilityOfElement(element);
        }
    }

    public void refreshPage() {
        driver.navigate().refresh();
        waitUntilPageLoaded();
    }

    public void clearBrowserStorageUsingJS() {
        sleep(2000);
        waitUntilPageLoaded();
        getExecutor().executeScript("window.sessionStorage.clear();");
        getExecutor().executeScript("window.localStorage.clear();");
    }

    public static void hoverOverElement(WebDriver driver, WebElement element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element).build().perform();
    }

}



