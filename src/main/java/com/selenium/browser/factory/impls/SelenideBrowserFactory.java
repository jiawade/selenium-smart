package com.selenium.browser.factory.impls;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.selenium.browser.factory.BrowserFactory;
import com.selenium.enums.BrowserType;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import static com.selenium.enums.BrowserType.*;

@Slf4j
public class SelenideBrowserFactory implements BrowserFactory {
    private WebDriver driver;

    public WebDriver setUp(BrowserType browserType) {
        if (browserType.equals(CHROME)) {
            return this.instanceChrome();
        } else if (browserType.equals(FIREFOX)) {
            return this.instanceFireFox();
        } else if (browserType.equals(EDGE)) {
            return this.instanceEdge();
        } else if (browserType.equals(SAFARI)) {
            return this.instanceSafari();
        } else {
            throw new IllegalArgumentException("no browser type found");
        }
    }

    public WebDriver getDriver() {
        return driver;
    }

    private synchronized ChromeDriver instanceChrome() {
        Configuration.browser = "chrome";
        Selenide.open();
        driver = Selenide.webdriver().driver().getWebDriver();
        return (ChromeDriver) driver;
    }

    private synchronized FirefoxDriver instanceFireFox() {
        Configuration.browser = "firefox";
        Selenide.open();
        driver = Selenide.webdriver().driver().getWebDriver();
        return (FirefoxDriver) driver;
    }

    private synchronized EdgeDriver instanceEdge() {
        Configuration.browser = "edge";
        Selenide.open();
        driver = Selenide.webdriver().driver().getWebDriver();
        return (EdgeDriver) driver;
    }

    private synchronized SafariDriver instanceSafari() {
        Configuration.browser = "safari";
        Selenide.open();
        driver = Selenide.webdriver().driver().getWebDriver();
        return (SafariDriver) driver;
    }
}
