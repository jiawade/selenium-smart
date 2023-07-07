package io.smart.browser.factory.impls;


import io.smart.browser.browsers.ChromeBrowser;
import io.smart.browser.browsers.EdgeBrowser;
import io.smart.browser.browsers.FirefoxBrowser;
import io.smart.browser.browsers.SafariBrowser;
import io.smart.browser.configuration.Configuration;
import io.smart.browser.factory.BrowserFactory;
import io.smart.enums.BrowserType;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

@Slf4j
public class SeleniumBrowser implements BrowserFactory {
    private WebDriver driver;


    @Override
    public SeleniumBrowser setUp(BrowserType browserType, Boolean headless) {
        if (browserType.equals(BrowserType.CHROME)) {
            this.instanceChrome(headless);
            return this;
        } else if (browserType.equals(BrowserType.FIREFOX)) {
            this.instanceFireFox(headless);
            return this;
        } else if (browserType.equals(BrowserType.EDGE)) {
            this.instanceEdge(headless);
            return this;
        }  else if (browserType.equals(BrowserType.SAFARI)) {
            this.instanceSafari(headless);
            return this;
        }else {
            throw new IllegalArgumentException("no browser type found");
        }
    }

    @Override
    public SeleniumBrowser setUp(BrowserType browserType, Configuration conf) {
        if (browserType.equals(BrowserType.CHROME)) {
            this.instanceChrome(conf);
            return this;
        } else if (browserType.equals(BrowserType.FIREFOX)) {
            this.instanceFireFox(conf);
            return this;
        } else if (browserType.equals(BrowserType.EDGE)) {
            this.instanceEdge(conf);
            return this;
        } else if (browserType.equals(BrowserType.SAFARI)) {
            this.instanceSafari(conf);
            return this;
        }else {
            throw new IllegalArgumentException("no browser type found");
        }
    }

    @Override
    public DevMode getDevTools() {
        return new DevMode(getDriver());
    }

    @Override
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
