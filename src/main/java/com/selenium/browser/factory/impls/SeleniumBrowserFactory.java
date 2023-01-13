package com.selenium.browser.factory.impls;


import com.selenium.browser.browsers.SafariBrowser;
import com.selenium.browser.configuration.Configuration;
import com.selenium.browser.browsers.ChromeBrowser;
import com.selenium.browser.browsers.EdgeBrowser;
import com.selenium.browser.browsers.FirefoxBrowser;
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
public class SeleniumBrowserFactory implements BrowserFactory {
    private WebDriver driver;


    public SeleniumBrowserFactory setUp(BrowserType browserType, Boolean headless) {
        if (browserType.equals(CHROME)) {
            this.instanceChrome(headless);
            return this;
        } else if (browserType.equals(FIREFOX)) {
            this.instanceFireFox(headless);
            return this;
        } else if (browserType.equals(EDGE)) {
            this.instanceEdge(headless);
            return this;
        }  else if (browserType.equals(SAFARI)) {
            this.instanceSafari(headless);
            return this;
        }else {
            throw new IllegalArgumentException("no browser type found");
        }
    }

    public SeleniumBrowserFactory setUp(BrowserType browserType, Configuration conf) {
        if (browserType.equals(CHROME)) {
            this.instanceChrome(conf);
            return this;
        } else if (browserType.equals(FIREFOX)) {
            this.instanceFireFox(conf);
            return this;
        } else if (browserType.equals(EDGE)) {
            this.instanceEdge(conf);
            return this;
        } else if (browserType.equals(SAFARI)) {
            this.instanceSafari(conf);
            return this;
        }else {
            throw new IllegalArgumentException("no browser type found");
        }
    }

    public Websocket getWebsocket() {
        return new Websocket(getDriver());
    }

    public WebDriver getDriver() {
        return driver;
    }

    private synchronized ChromeDriver instanceChrome(Boolean headless) {
        driver=new ChromeBrowser().setUpBrowser(headless);
        return (ChromeDriver) driver ;
    }

    private synchronized ChromeDriver instanceChrome(Configuration conf) {
        driver=new ChromeBrowser().setUpBrowser(conf);
        return (ChromeDriver) driver ;
    }

    private synchronized FirefoxDriver instanceFireFox(Boolean headless) {
        driver=new FirefoxBrowser().setUpBrowser(headless);
        return (FirefoxDriver) driver ;
    }

    private synchronized FirefoxDriver instanceFireFox(Configuration conf) {
        driver=new FirefoxBrowser().setUpBrowser(conf);
        return (FirefoxDriver) driver ;
    }

    private synchronized EdgeDriver instanceEdge(Boolean headless) {
        driver=new EdgeBrowser().setUpBrowser(headless);
        return (EdgeDriver) driver ;
    }

    private synchronized EdgeDriver instanceEdge(Configuration conf) {
        driver=new EdgeBrowser().setUpBrowser(conf);
        return (EdgeDriver) driver ;
    }

    private synchronized SafariDriver instanceSafari(Boolean headless) {
        driver=new SafariBrowser().setUpBrowser(headless);
        return (SafariDriver) driver ;
    }

    private synchronized SafariDriver instanceSafari(Configuration conf) {
        driver=new SafariBrowser().setUpBrowser(conf);
        return (SafariDriver) driver ;
    }

}
