package io.smart.driver;

import io.smart.utils.tools.Tools;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;

import java.util.*;


@Slf4j
public class DriverOperation {
    public static WebDriver driver;
    public static Actions actions;
    public static JavascriptExecutor js;
    public static int secondTimeOut = 30;
    public static final int interval = 100;



    public DriverOperation(@NonNull WebDriver driver) {
        DriverOperation.driver = driver;
        actions = new Actions(driver);
        js = (JavascriptExecutor) driver;
    }

    public DriverOperation(@NonNull WebDriver driver, @NonNull WebDriverEventListener listener) {
        DriverOperation.driver = new EventFiringWebDriver(driver).register(listener);
        actions = new Actions(driver);
        js = (JavascriptExecutor) driver;
    }

    public void setWebDriver(@NonNull WebDriver driver) {
        DriverOperation.driver = driver;
        actions = new Actions(driver);
        js = (JavascriptExecutor) driver;
    }

    public void setWebDriver(@NonNull WebDriver driver, @NonNull WebDriverEventListener listener) {
        DriverOperation.driver = new EventFiringWebDriver(driver).register(listener);
        actions = new Actions(driver);
        js = (JavascriptExecutor) driver;
    }

    public void get(@NonNull String url) {
        driver.get(url);
    }

    public void closeBrowser() {
        driver.quit();
    }

    public void closeBrowserTab() {
        driver.close();
    }

    public String getPageSource() {
        return driver.getPageSource();
    }

    public String getWindowHandle() {
        return driver.getWindowHandle();

    }

    public Set<String> getWindowHandles() {
        return driver.getWindowHandles();

    }

    public String getTitle() {
        return driver.getTitle();
    }

    public void previousPage() {
        driver.navigate().back();
    }

    public void forwardPage() {
        driver.navigate().forward();
    }

    public void refreshBrowser() {
        driver.navigate().refresh();
    }

    public WebDriver.Options manage(){
        return driver.manage();
    }

    public WebDriver.TargetLocator switchTo(){
        return driver.switchTo();
    }

    public void createBrowserWindow() {
        driver.switchTo().newWindow(WindowType.WINDOW);
    }

    public void createBrowserTab() {
        driver.switchTo().newWindow(WindowType.TAB);
    }

    public void switchToFirstBrowserTab() {
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(0));
    }

    public void switchToLastBrowserTab() {
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(tabs.size() - 1));
    }

    public void switchToBrowserTab(int index) {
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(index));
    }

    public boolean setWindowSize(int width, int height) {
        Dimension expectedSize = new Dimension(width, height);
        driver.manage().window().setSize(expectedSize);
        Dimension actualSize = driver.manage().window().getSize();
        return expectedSize.toString().equals(actualSize.toString());
    }

    public Object takeScreenshot(WebElement webElement, OutputType<?> outputType) {
        return webElement.getScreenshotAs(outputType);
    }

    public Object takeScreenshot(OutputType<?> outputType) {
        Object screenshot = null;
        if (driver instanceof ChromeDriver) {
            screenshot = ((ChromeDriver) driver).getScreenshotAs(outputType);
        } else if (driver instanceof FirefoxDriver) {
            screenshot = ((FirefoxDriver) driver).getScreenshotAs(outputType);
        } else if (driver instanceof EdgeDriver) {
            screenshot = ((EdgeDriver) driver).getScreenshotAs(outputType);
        }
        return screenshot;
    }

    public void setSessionItem(String key, String value) {
        if (driver instanceof ChromeDriver) {
            ((ChromeDriver) driver).getSessionStorage().setItem(key, value);
        } else if (driver instanceof FirefoxDriver) {
            ((FirefoxDriver) driver).getSessionStorage().setItem(key, value);
        } else if (driver instanceof EdgeDriver) {
            ((EdgeDriver) driver).getSessionStorage().setItem(key, value);
        }
    }

    public String getSessionItem(String key) {
        String value = null;
        ChromeDriver d = (ChromeDriver) driver;
        for (int i = 0; i <= 30; i++) {
            value = d.getSessionStorage().getItem(key);
            if (value != null) {
                break;
            } else {
                Tools.sleep(1000);
            }
        }
        return value;
    }

}
